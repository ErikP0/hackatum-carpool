package de.hackatum2018.sixtcarpool.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.hackatum2018.sixtcarpool.R

class OpenCarpoolActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_carpool)

        val id = intent.getIntExtra(RENTAL_ID, -1)
        if (id == -1) {
            throw IllegalArgumentException("Expected rental id")
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.open_carpool_fragment_container, OpenCarpoolFragment.newInstance(id))
                .commit()
        }
    }

    companion object {
        const val RENTAL_ID = "RENTAL_ID"
    }
}
