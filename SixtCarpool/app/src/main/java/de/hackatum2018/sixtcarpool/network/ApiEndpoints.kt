package de.hackatum2018.sixtcarpool.network

import de.hackatum2018.sixtcarpool.database.entities.CarRental
import de.hackatum2018.sixtcarpool.database.entities.CarpoolOffer
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Aleksandr Kim on 17 Nov, 2018 6:16 PM for SixtCarpool
 */
interface ApiEndpoints {

    @GET("get-rentals-by-id")
    fun getCarRentals(@Query("userId") userId: Int): Single<List<CarRental>>

    @GET("get-all-rentals")
    fun getAllCarRentals(): Single<List<CarRental>>

    @GET("get-offers-by-id")
    fun getCarpoolOffers(@Query("userId") userId: Int): Single<List<CarpoolOffer>>

    @GET("get-all-offers")
    fun getAllCarpoolOffers(): Single<List<CarpoolOffer>>

}