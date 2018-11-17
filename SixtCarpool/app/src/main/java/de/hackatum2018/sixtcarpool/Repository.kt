package de.hackatum2018.sixtcarpool

import android.util.Log
import de.hackatum2018.sixtcarpool.Util.SingletonHolder
import de.hackatum2018.sixtcarpool.database.AppDatabase
import de.hackatum2018.sixtcarpool.database.dao.CarpoolOfferDao
import de.hackatum2018.sixtcarpool.database.dao.MyRentalDao
import de.hackatum2018.sixtcarpool.database.entities.CarRental
import de.hackatum2018.sixtcarpool.database.entities.CarpoolOffer
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by Aleksandr Kim on 17 Nov, 2018 2:42 PM for SixtCarpool
 */
class Repository(private val appDatabase: AppDatabase) {

    private val myRentalDao: MyRentalDao = appDatabase.myRentalDao()
    private val carpoolOfferDao: CarpoolOfferDao = appDatabase.carpoolOfferDao()

//    fun addMyRental(carRental: CarRental) {
//
//    }

    fun getMyRentalsAll(): Flowable<List<CarRental>> {
        Log.d(TAG, "getMyRentalsAll: ")
        return myRentalDao.getAll()
    }

    fun getMyRentalById(id: Int): Flowable<CarRental?> {
        return myRentalDao.getById(id)
    }

    fun getCarpoolOffersAll(): Flowable<List<CarpoolOffer>> {
        Log.d(TAG, "getMyRentalsAll: ")
        return carpoolOfferDao.getAll()
    }

    fun getCarpoolOfferById(id: Int): Flowable<CarpoolOffer?> {
        return carpoolOfferDao.getById(id)
    }

    fun addCarpoolOffer(carpoolOffer: CarpoolOffer): Completable = Completable.fromAction {
        carpoolOfferDao.add(carpoolOffer)
    }

    companion object : SingletonHolder<AppDatabase, Repository>(::Repository) {
        const val TAG = "Repository"
    }

}