package de.hackatum2018.sixtcarpool.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Aleksandr Kim on 17 Nov, 2018 12:27 PM for SixtCarpool
 */
@Entity(tableName = CarRental.TABLE_NAME)
data class CarRental(@ColumnInfo(name = ID) @PrimaryKey val id: Int,
                     val name: String,
                     @ColumnInfo(name = MAX_PASSENGERS_COLUMN) val maxPassengers: Int,
                     val imageUrl: String,
                     val pickupStationId: Long,
                     val pickupStationName: String,
                     val pickupTime: Long,
                     val returnStationId: Long,
                     val returnStationName: String,
                     val returnTime: Long,
                     val price: Long,
                     @ColumnInfo(name = CARPOOL_OFFER_ID) var carpoolOfferId: Int?
) {

    companion object {
        const val TABLE_NAME = "my_rentals"
        const val ID = "car_rental_id"
        const val MAX_PASSENGERS_COLUMN = "maxPassengers_car"
        const val CARPOOL_OFFER_ID = "carpool_offer_id"

        fun dummy(rentalId: Int): CarRental {
            return CarRental(rentalId, "my rented car", 4, "", 1,
                "center", 1234, 2, "campus", 2345, 100, null)
        }
    }

    @Ignore
    val isOffered: Boolean = carpoolOfferId != null
}