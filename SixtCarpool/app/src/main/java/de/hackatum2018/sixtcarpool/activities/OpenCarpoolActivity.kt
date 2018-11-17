package de.hackatum2018.sixtcarpool.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import de.hackatum2018.sixtcarpool.R

class OpenCarpoolActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_carpool)

        if(savedInstanceState == null)  {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.open_carpool_fragment_container, OpenCarpoolFragment.newInstance())
                .commit()
        }
    }
}
