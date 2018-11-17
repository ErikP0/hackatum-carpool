package de.hackatum2018.sixtcarpool.database.entities

import android.arch.persistence.room.Entity

/**
 * Created by Aleksandr Kim on 17 Nov, 2018 1:22 PM for SixtCarpool
 */
@Entity(tableName = CarpoolOffer.TABLE_NAME)
data class CarpoolOffer(val placeFrom: String,
                        val placeTo: String,
                        val startTime: String,
                        val startDate: String,
                        val maxPassengers: Int,
                        val pricePerPassenger: Int
) {
    companion object {
        const val TABLE_NAME = "carpool_offers"
    }
}