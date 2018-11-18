package de.hackatum2018.sixtcarpool.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import de.hackatum2018.sixtcarpool.R

class CarpoolDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carpool_detail)
    }

    companion object {
        const val OFFER_ID = "OFFER_ID"
    }
}
