package de.hackatum2018.sixtcarpool.database.entities

import android.arch.persistence.room.*

/**
 * Created by Aleksandr Kim on 17 Nov, 2018 12:27 PM for SixtCarpool
 */
@Entity(tableName = CarRental.TABLE_NAME)
data class CarRental(@PrimaryKey val id: Int,
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
                     @Embedded var carpoolOffer: CarpoolOffer?
) {

    companion object {
        const val TABLE_NAME = "my_rentals"
        const val MAX_PASSENGERS_COLUMN = "maxPassengers_car"

        fun dummy(rentalId: Int): CarRental {
            return CarRental(rentalId, "my rented car", 4, "", 1,
                "center", 1234, 2, "campus", 2345, 100, null)
        }

        fun dummyWithOffer(rentalId: Int, carpoolOfferId: Int): CarRental {
            return dummy(rentalId).apply { carpoolOffer = CarpoolOffer.dummy(carpoolOfferId, rentalId) }
        }
    }

    @Ignore
    val isOffered: Boolean = carpoolOffer != null
}