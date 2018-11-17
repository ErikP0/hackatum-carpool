package de.hackatum2018.sixtcarpool.database.entities

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Aleksandr Kim on 17 Nov, 2018 12:27 PM for SixtCarpool
 */
@Entity(tableName = MyCarRentalEntity.TABLE_NAME)
data class MyCarRentalEntity(@PrimaryKey val id: String,
                             val name: String,
                             val maxPassengers: Int,
                             val imageUrl: String,
                             val pickupStation: Long,
                             val pickupStationName: String,
                             val pickupTime: String,
                             val returnStation: Long,
                             val returnStationName: String,
                             val returnTime: String,
                             val price: Long,
                             @Embedded val carpoolOffer: CarpoolOffer?
) {

    companion object {
        const val TABLE_NAME = "my_car_rentals"
    }

    val isOffered: Boolean = carpoolOffer != null
}