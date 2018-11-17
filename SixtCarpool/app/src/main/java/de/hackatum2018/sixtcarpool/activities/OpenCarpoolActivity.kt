package de.hackatum2018.sixtcarpool.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import de.hackatum2018.sixtcarpool.R
import java.lang.IllegalArgumentException

class OpenCarpoolActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_carpool)

        val id = intent.getStringExtra(RENTAL_ID)
        if(id.isEmpty()) {
            throw IllegalArgumentException("Expected rental id")
        }

        if(savedInstanceState == null)  {
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
