package de.hackatum2018.sixtcarpool.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.hackatum2018.sixtcarpool.R

class CarpoolDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carpool_detail)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.carpool_detail_fragment_contaier, CarpoolDetailFragment.newInstance(0))
                .commit()
        }
    }

    companion object {
        const val OFFER_ID = "OFFER_ID"
    }
}
