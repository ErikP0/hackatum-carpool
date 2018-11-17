package de.hackatum2018.sixtcarpool

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import de.hackatum2018.sixtcarpool.database.AppDatabase
import de.hackatum2018.sixtcarpool.database.entities.CarRental
import de.hackatum2018.sixtcarpool.util.commonSchedulers

class MainActivity : AppCompatActivity() {

    private lateinit var repository : Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repository = Repository.getInstance(AppDatabase.getInstance(applicationContext))

        repository.clearCarRentalDb().commonSchedulers().subscribe {
            Log.d(TAG, "carRentalDb is cleared ")
            debugAddRentals()
        }

    }

    private fun debugAddRentals() {
        repository.addMyRental(listOf(CarRental.dummyWithOffer(0, 0), CarRental.dummy(1))).commonSchedulers()
            .doOnError { t -> Log.d(TAG, "added error: ", t) }
            .doOnComplete { debugShowAllRentalsLog()}
            .subscribe { Log.d(TAG, "added car rental") }
    }

    private fun debugShowAllRentalsLog() {
        repository.getMyRentalsAll().commonSchedulers()
            .subscribe { it.forEach { Log.d(TAG, "all rentals: " + it + "\n") } }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}


