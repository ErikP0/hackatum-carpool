package de.hackatum2018.sixtcarpool.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Aleksandr Kim on 17 Nov, 2018 1:22 PM for SixtCarpool
 */
@Entity(tableName = CarpoolOffer.TABLE_NAME)
data class CarpoolOffer(@ColumnInfo(name = PLACE_FROM_COLUMN) var placeFrom: String,
                        @ColumnInfo(name = PLACE_TO_COLUMN)  var placeTo: String,
                        @ColumnInfo(name = START_TIME_COLUMN) var startTime: Long,
                        @ColumnInfo(name = START_DATE_COLUMN) var startDate: Long,
                        @ColumnInfo(name = MAX_PASSENGERS_COLUMN) var maxPassengers: Int,
                        @ColumnInfo(name = PRICE_PER_PASSENGER_COLUMN) var pricePerPassenger: Int,
                        @ColumnInfo(name = ID_COLUMN) @PrimaryKey val id: Int = 0,
                        @ColumnInfo(name = CAR_RENTAL_ID) val carRentalId: Int
) {
    companion object {
        const val TABLE_NAME = "carpool_offers"
        const val PLACE_FROM_COLUMN = "place_from"
        const val PLACE_TO_COLUMN = "place_to"
        const val START_TIME_COLUMN = "start_time"
        const val START_DATE_COLUMN = "start_date"
        const val MAX_PASSENGERS_COLUMN = "maxPassengers_carpool"
        const val PRICE_PER_PASSENGER_COLUMN = "price_per_pass"
        const val ID_COLUMN = "carpool_offer_id"
        const val CAR_RENTAL_ID = "car_rental_id"

        fun dummy(carpoolOfferId: Int, carRentalId: Int): CarpoolOffer {
            return CarpoolOffer("Marienplatz", "TUM", 6789, 7890, 3, 25, carpoolOfferId, carRentalId)
        }
    }
}