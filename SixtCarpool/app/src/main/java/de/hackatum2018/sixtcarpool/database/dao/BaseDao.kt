package de.hackatum2018.sixtcarpool.database.dao

import android.arch.persistence.room.*

/**
 * Created by Aleksandr Kim on 17 Nov, 2018 1:59 PM for SixtCarpool
 */
@Dao
abstract class BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun add(t: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun add(vararg t: T): LongArray

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract fun update(t: T)

    @Transaction
    open fun addOrUpdate(t: T) {
        add(t)
        update(t)
    }

    @Delete
    abstract fun delete(t: T)

    open fun add(t: List<T>) {
        for (i in t) add(i)
    }

    abstract fun count(): Int

    companion object {
        const val TAG = "BaseDao"
    }
}
