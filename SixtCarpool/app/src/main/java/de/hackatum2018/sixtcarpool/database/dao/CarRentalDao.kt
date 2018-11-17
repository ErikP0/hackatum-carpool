package de.hackatum2018.sixtcarpool.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import de.hackatum2018.sixtcarpool.database.entities.CarRental
import io.reactivex.Flowable

/**
 * Created by Aleksandr Kim on 17 Nov, 2018 1:59 PM for SixtCarpool
 */
@Dao
abstract class CarRentalDao : BaseDao<CarRental>() {

    @Query("SELECT * FROM " + CarRental.TABLE_NAME)
    abstract fun getAll(): Flowable<List<CarRental>>

    @Query("SELECT * FROM " + CarRental.TABLE_NAME + " WHERE id = :id")
    abstract fun getById(id: Int): Flowable<CarRental?>

    @Query("DELETE FROM " + CarRental.TABLE_NAME)
    abstract fun deleteAll()

    @Query("DELETE FROM " + CarRental.TABLE_NAME + " WHERE id = :id")
    abstract fun deleteById(id: String)

//    @Query("UPDATE " + CarRental.TABLE_NAME + " SET carpoolOffer = :carpoolOffer  WHERE id = :id")
//    abstract fun setCarpoolOffer(id: Int, carpoolOffer: CarpoolOffer)

    @Transaction
    open fun cleanUpdate(list: List<CarRental>) {
        deleteAll()
        add(list)
    }

    @Query("SELECT COUNT(*) FROM " + CarRental.TABLE_NAME)
    abstract override fun count(): Int
}
