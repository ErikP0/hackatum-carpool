package de.hackatum2018.sixtcarpool.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import de.hackatum2018.sixtcarpool.BuildConfig
import de.hackatum2018.sixtcarpool.Util.SingletonHolder
import de.hackatum2018.sixtcarpool.database.dao.CarpoolOfferDao
import de.hackatum2018.sixtcarpool.database.dao.MyRentalDao
import de.hackatum2018.sixtcarpool.database.entities.CarRental
import de.hackatum2018.sixtcarpool.database.entities.CarpoolOffer

/**
 * Created by Aleksandr Kim on 17 Nov, 2018 2:37 PM for SixtCarpool
 */
@Database(entities = arrayOf(CarRental::class, CarpoolOffer::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun myRentalDao(): MyRentalDao
    abstract fun carpoolOfferDao(): CarpoolOfferDao

    companion object : SingletonHolder<Context, AppDatabase>({
        Room.databaseBuilder(it.applicationContext, AppDatabase::class.java, BuildConfig.APPLICATION_ID).build()
    })

}
