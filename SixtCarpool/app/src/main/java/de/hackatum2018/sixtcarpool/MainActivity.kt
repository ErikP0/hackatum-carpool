package de.hackatum2018.sixtcarpool

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.hackatum2018.sixtcarpool.database.AppDatabase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val repository = Repository.getInstance(AppDatabase.getInstance(applicationContext))


    }

    companion object {
        const val TAG = "MainActivity"
    }
}


