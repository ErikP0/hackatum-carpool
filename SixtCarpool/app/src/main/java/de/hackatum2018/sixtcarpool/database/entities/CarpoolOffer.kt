package de.hackatum2018.sixtcarpool.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Aleksandr Kim on 17 Nov, 2018 1:22 PM for SixtCarpool
 */
@Entity(tableName = CarpoolOffer.TABLE_NAME)
data class CarpoolOffer(var placeFrom: String,
                        var placeTo: String,
                        var startTime: Long,
                        var startDate: Long,
                        @ColumnInfo(name = MAX_PASSENGERS_COLUMN) var maxPassengers: Int,
                        var pricePerPassenger: Int,
                        @ColumnInfo(name = ID_COLUMN) @PrimaryKey val id: Int,
                        val carRentalId: Int
) {
    companion object {
        const val TABLE_NAME = "carpool_offers"
        const val ID_COLUMN = "carpool_offer_id"
        const val MAX_PASSENGERS_COLUMN = "maxPassengers_carpool"

        fun dummy(carpoolOfferId: Int, carRentalId: Int): CarpoolOffer {
            return CarpoolOffer("Marienplatz", "TUM", 6789, 7890, 3, 25, carpoolOfferId, carRentalId)
        }
    }
}