package de.hackatum2018.sixtcarpool

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import de.hackatum2018.sixtcarpool.activities.DashboardFragment
import de.hackatum2018.sixtcarpool.activities.RentalListFragment
import de.hackatum2018.sixtcarpool.database.AppDatabase

class MainActivity : AppCompatActivity() {
    private val fragments: MutableMap<Int, FragmentInfo> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val repository = Repository.getInstance(AppDatabase.getInstance(applicationContext))

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

    companion object {
        const val TAG = "MainActivity"
    }

    private class FragmentInfo(val fragment: Fragment, val title: String)
}
