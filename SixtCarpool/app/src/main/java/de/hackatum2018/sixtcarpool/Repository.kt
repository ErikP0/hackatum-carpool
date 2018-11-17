package de.hackatum2018.sixtcarpool

import android.util.Log
import de.hackatum2018.sixtcarpool.database.AppDatabase
import de.hackatum2018.sixtcarpool.database.dao.CarRentalDao
import de.hackatum2018.sixtcarpool.database.dao.CarpoolOfferDao
import de.hackatum2018.sixtcarpool.database.entities.CarRental
import de.hackatum2018.sixtcarpool.database.entities.CarpoolOffer
import de.hackatum2018.sixtcarpool.util.SingletonHolder
import de.hackatum2018.sixtcarpool.util.commonSchedulers
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Aleksandr Kim on 17 Nov, 2018 2:42 PM for SixtCarpool
 */
class Repository(private val appDatabase: AppDatabase) {

    private val carRentalDao: CarRentalDao = appDatabase.myRentalDao()
    private val carpoolOfferDao: CarpoolOfferDao = appDatabase.carpoolOfferDao()

    fun addMyRental(carRental: CarRental): Completable {
        return Completable.fromCallable { carRentalDao.add(carRental) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun addMyRental(carRentalList: List<CarRental>): Completable {
//        return carRentalDao.add(carRentalList).makeCompletable()
        return Completable.fromCallable { carRentalDao.add(carRentalList) }.commonSchedulers()
    }

    fun getMyRentalsAll(): Flowable<List<CarRental>> {
        Log.d(TAG, "getMyRentalsAll: ")
        return carRentalDao.getAll()
    }

    fun getMyRentalById(id: Int): Flowable<CarRental?> {
        return carRentalDao.getById(id)
    }

    fun getCarpoolOffersAll (): Flowable<List<CarpoolOffer>> {
        Log.d(TAG, "getMyRentalsAll: ")
        return carpoolOfferDao.getAll()
    }

    fun getCarpoolOfferById(id: Int): Flowable<CarpoolOffer?> {
        return carpoolOfferDao.getById(id)
    }

    fun clearCarRentalDb(): Completable {
//        return carRentalDao.deleteAll().makeCompletable()
        return Completable.fromCallable{carRentalDao.deleteAll()}.commonSchedulers()
    }

    companion object : SingletonHolder<AppDatabase, Repository>(::Repository) {
        const val TAG = "Repository"
    }

}