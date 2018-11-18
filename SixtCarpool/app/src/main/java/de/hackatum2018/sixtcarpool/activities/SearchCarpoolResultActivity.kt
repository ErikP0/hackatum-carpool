package de.hackatum2018.sixtcarpool.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.hackatum2018.sixtcarpool.R

class SearchCarpoolResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_carpool_result)

        if (savedInstanceState == null) {
            val radius = intent.getIntExtra(WALKING_RADIUS, 5)
            val from = intent.getStringExtra(FROM)
            val to = intent.getStringExtra(TO)

            supportFragmentManager
                .beginTransaction()
                .add(R.id.search_carpool_fragment_container, SearchCarpoolResultFragment.newInstance(from, to, radius))
                .commit()

        }
    }

    companion object {
        const val WALKING_RADIUS = "WALKING_RADIUS"
        const val FROM = "FROM"
        const val TO = "TO"
    }
}
