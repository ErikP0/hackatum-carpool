package de.hackatum2018.sixtcarpool.util

/**
 * Created by Aleksandr Kim on 17 Nov, 2018 2:39 PM for SixtCarpool
 */
open class SingletonHolder<in A, out T>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}