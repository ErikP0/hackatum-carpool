package de.hackatum2018.sixtcarpool

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import de.hackatum2018.sixtcarpool.activities.DashboardFragment
import de.hackatum2018.sixtcarpool.activities.RentalListFragment
import de.hackatum2018.sixtcarpool.database.AppDatabase
import de.hackatum2018.sixtcarpool.database.entities.CarRental
import de.hackatum2018.sixtcarpool.database.entities.CarpoolOffer
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    private val fragments: MutableMap<Int, FragmentInfo> = mutableMapOf()

    private lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repository = Repository.getInstance(AppDatabase.getInstance(applicationContext))

        repository.clearCarRentalDb().subscribe {
            Log.d(TAG, "carRentalDb is cleared ")
            debugAddRentals()
        }

        val drawerLayout = findViewById<DrawerLayout>(R.id.main_drawer)

        val navigationView = findViewById<NavigationView>(R.id.main_navigation_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            onNavigation(menuItem.itemId)
            true
        }

        //set my rentals as default
        navigationView.setCheckedItem(R.id.menu_my_rentals)
        onNavigation(R.id.menu_my_rentals)
    }

    private fun onNavigation(@IdRes menuItemId: Int) {
        val fragmentInfo = when (menuItemId) {
            //R.id.menu_my_carpool -> fragments.computeIfAbsent(R.id.menu_my_carpool) {  }
            R.id.menu_my_rentals -> fragments.computeIfAbsent(R.id.menu_my_rentals) {
                FragmentInfo(
                    RentalListFragment.newInstance(),
                    "My rentals"
                )
            }
            R.id.menu_dashboard -> fragments.computeIfAbsent(R.id.menu_dashboard) {
                FragmentInfo(DashboardFragment.newInstance(), "Dashboard")
            }
            else -> throw IllegalArgumentException("Unknown id $menuItemId")
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_fragment_container, fragmentInfo.fragment)
            .commit()
        supportActionBar?.title = fragmentInfo.title

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

    private class FragmentInfo(val fragment: Fragment, val title: String)
}


