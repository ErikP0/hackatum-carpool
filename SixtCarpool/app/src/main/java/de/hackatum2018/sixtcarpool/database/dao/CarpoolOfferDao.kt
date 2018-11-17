package de.hackatum2018.sixtcarpool.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import de.hackatum2018.sixtcarpool.database.entities.CarpoolOffer
import io.reactivex.Flowable

/**
 * Created by Aleksandr Kim on 17 Nov, 2018 2:36 PM for SixtCarpool
 */
@Dao
abstract class CarpoolOfferDao : BaseDao<CarpoolOffer>() {

    @Query("SELECT * FROM " + CarpoolOffer.TABLE_NAME)
    abstract fun getAll(): Flowable<List<CarpoolOffer>>

    @Query("SELECT * FROM " + CarpoolOffer.TABLE_NAME + " WHERE " + CarpoolOffer.ID_COLUMN + "  = :id")
    abstract fun getById(id: Int): Flowable<CarpoolOffer?>

    @Query("DELETE FROM " + CarpoolOffer.TABLE_NAME)
    abstract fun deleteAll()

    @Query("DELETE FROM " + CarpoolOffer.TABLE_NAME + " WHERE " + CarpoolOffer.ID_COLUMN + " = :id")
    abstract fun deleteById(id: Int)

    @Transaction
    open fun cleanUpdate(list: List<CarpoolOffer>) {
        deleteAll()
        add(list)
    }

    @Query("SELECT COUNT(*) FROM " + CarpoolOffer.TABLE_NAME)
    abstract override fun count(): Int
}
