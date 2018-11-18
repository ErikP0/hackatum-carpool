package de.hackatum2018.sixtcarpool.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Aleksandr Kim on 17 Nov, 2018 5:50 PM for SixtCarpool
 */
object NetworkProvider {
    const val TAG = "NetworkProvider"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .addInterceptor { chain ->
                val request = chain.request().newBuilder().addHeader("Authorization", NetworkConstants.TOKEN).build()
                chain.proceed(request)
            }
            .build()
    }

    private val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(NetworkConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun <S> getService(serviceClass: Class<S>): S {
        return retrofitInstance.create(serviceClass)
    }

}