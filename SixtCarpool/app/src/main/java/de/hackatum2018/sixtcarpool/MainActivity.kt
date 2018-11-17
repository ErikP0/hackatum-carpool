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
import de.hackatum2018.sixtcarpool.util.commonSchedulers

class MainActivity : AppCompatActivity() {
    private val fragments: MutableMap<Int, FragmentInfo> = mutableMapOf()

    private lateinit var repository: Repository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repository = Repository.getInstance(AppDatabase.getInstance(applicationContext))

        repository.clearCarRentalDb().commonSchedulers().subscribe {
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
        repository.addMyRental(listOf(CarRental.dummyWithOffer(0, 0), CarRental.dummy(1))).commonSchedulers()
            .doOnError { t -> Log.d(TAG, "added error: ", t) }
            .doOnComplete { debugShowAllRentalsLog() }
            .subscribe { Log.d(TAG, "added car rental") }
    }

    private fun debugShowAllRentalsLog() {
        repository.getMyRentalsAll().commonSchedulers()
            .subscribe { it.forEach { Log.d(TAG, "all rentals: " + it + "\n") } }
    }

    companion object {
        const val TAG = "MainActivity"
    }

    private class FragmentInfo(val fragment: Fragment, val title: String)
}


