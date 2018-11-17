package de.hackatum2018.sixtcarpool

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import de.hackatum2018.sixtcarpool.database.AppDatabase
import de.hackatum2018.sixtcarpool.database.entities.CarRental
import de.hackatum2018.sixtcarpool.database.entities.CarpoolOffer
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repository = Repository.getInstance(AppDatabase.getInstance(applicationContext))

        repository.clearCarRentalDb().subscribe {
            Log.d(TAG, "carRentalDb is cleared ")
            debugAddRentals()
        }
    }

    private fun debugAddRentals() {
        val carpoolOfferDummy = CarpoolOffer.dummy(0, 0)
        repository.addMyRental(CarRental.dummy(1)).subscribeOn(Schedulers.io()).subscribe { Log.d(TAG, "car rental 1 is inserted ") }
        repository.addMyRental(CarRental.dummy(2)).subscribeOn(Schedulers.io()).subscribe { Log.d(TAG, "car rental 2 is inserted ") }
        repository.addMyRental(CarRental.dummy(3)).subscribeOn(Schedulers.io()).subscribe { Log.d(TAG, "car rental 3 is inserted ") }
        repository.addMyRental(CarRental.dummy(0))
            .subscribeOn(Schedulers.io())
            .doOnError { t -> Log.d(TAG, "added error: ", t) }
            .doOnComplete { Log.d(TAG, "added rentals Completed") }
            .andThen(Completable.defer { repository.addCarpoolOfferAndUpdateLinks(carpoolOfferDummy) })
            .subscribe {
                Log.d(TAG, "added car rental and carpool offer")
                debugShowAllRentalsLog()
            }
    }

    private fun addRentalOffer() {

    }

    private fun debugShowAllRentalsLog() {
        repository.getMyRentalsAll().subscribeOn(Schedulers.io()).subscribe { it.forEach { Log.d(TAG, "all rentals: " + it + "\n") } }
    }

    companion object {
        const val TAG = "MainActivityTUM"
    }
}


