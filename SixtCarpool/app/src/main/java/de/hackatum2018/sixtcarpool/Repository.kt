package de.hackatum2018.sixtcarpool

import android.util.Log
import de.hackatum2018.sixtcarpool.database.AppDatabase
import de.hackatum2018.sixtcarpool.database.dao.CarRentalDao
import de.hackatum2018.sixtcarpool.database.dao.CarpoolOfferDao
import de.hackatum2018.sixtcarpool.database.entities.CarRental
import de.hackatum2018.sixtcarpool.database.entities.CarpoolOffer
import de.hackatum2018.sixtcarpool.network.ApiEndpoints
import de.hackatum2018.sixtcarpool.network.NetworkProvider
import de.hackatum2018.sixtcarpool.util.SingletonHolder
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * Created by Aleksandr Kim on 17 Nov, 2018 2:42 PM for SixtCarpool
 */
class Repository(appDatabase: AppDatabase) {

    private val carRentalDao: CarRentalDao = appDatabase.myRentalDao()
    private val carpoolOfferDao: CarpoolOfferDao = appDatabase.carpoolOfferDao()

    private val apiEndpoints: ApiEndpoints by lazy { NetworkProvider.getService(ApiEndpoints::class.java) }

    fun getPairsOfRentalsAndOffers(includeNull: Boolean): PublishSubject<List<Pair<CarRental, CarpoolOffer?>>> {
        val res = PublishSubject.create<List<Pair<CarRental, CarpoolOffer?>>>()
        getMyRentalsAll().subscribeOn(Schedulers.io()).firstOrError()
            .subscribe { carRentals ->
                getCarpoolOffersAll().subscribe { carpoolOffers ->
                    val listOfPairs = mutableListOf<Pair<CarRental, CarpoolOffer?>>()
                    for (car in carRentals) {
                        val pairToSend = Pair(car, carpoolOffers.firstOrNull { it -> it.carRentalId == car.id })
                        if (!includeNull) {
                            if (pairToSend.second != null)
                                listOfPairs.add(pairToSend)
                        } else
                            listOfPairs.add(pairToSend)
                    }
//                    if (!includeNull) {
//                        val newRes = listOfPairs.filter { pair -> pair.second != null }
//                        res.onNext(newRes)
//                        Log.d(TAG, "only offers as pairs: " + newRes)
//                    } else
                        res.onNext(listOfPairs)
                    Log.d(TAG, "getPairsOfRentalsAndOffers: " + listOfPairs)
                }
            }
        return res
    }

    fun getMyRentalsFromApi(userId: Int): Single<List<CarRental>> {
        return apiEndpoints.getCarRentals(userId)
    }

    fun addMyRental(carRental: CarRental): Completable {
        return Completable.fromCallable { carRentalDao.add(carRental) }
    }

    fun addMyRental(carRentalList: List<CarRental>): Completable {
        Log.d(TAG, "addMyRental: started")
        return Completable.fromCallable { carRentalDao.add(carRentalList) }
    }

    fun getMyRentalsAll(): Flowable<List<CarRental>> {
        Log.d(TAG, "getMyRentalsAll: ")
        return carRentalDao.getAll()
    }

    fun getMyRentalById(id: Int): Flowable<CarRental?> {
        return carRentalDao.getById(id)
    }

    fun addCarpoolOffer(carpoolOffer: CarpoolOffer): Completable {
        Log.d(TAG, "addCarpoolOffer: $carpoolOffer")
        return Completable.fromCallable { carpoolOfferDao.add(carpoolOffer) }
    }

    fun addCarpoolOfferAndUpdateLinks(carpoolOffer: CarpoolOffer): Completable {
        Log.d(TAG, "addCarpoolOfferAndUpdateLinks: $carpoolOffer")
        return addCarpoolOffer(carpoolOffer).andThen(
            Completable.defer { linkCarToOffer(carpoolOffer.carRentalId, carpoolOffer.id) }
        )
    }

    fun linkCarToOffer(carId: Int, carpoolOfferId: Int): Completable {
        Log.d(TAG, "linkCarToOffer: begin $carId - $carpoolOfferId")
        return Completable.fromCallable { carRentalDao.linkCarToOffer(carId, carpoolOfferId) }
    }

    fun getCarpoolOffersAll(): Flowable<List<CarpoolOffer>> {
        Log.d(TAG, "getMyRentalsAll: ")
        return carpoolOfferDao.getAll()
    }

    fun getCarpoolOfferById(id: Int): Flowable<CarpoolOffer?> {
        return carpoolOfferDao.getById(id)
    }

    fun clearCarRentalDb(): Completable {
        return Completable.fromCallable { carRentalDao.deleteAll() }
    }

    fun clearCarpoolOfferDb(): Completable {
        return Completable.fromCallable { carpoolOfferDao.deleteAll() }
    }

    companion object : SingletonHolder<AppDatabase, Repository>(::Repository) {
        const val TAG = "RepositoryTUM"
    }

}