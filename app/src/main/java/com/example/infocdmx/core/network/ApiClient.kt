package com.example.infocdmx.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()


    private const val APIMETRO_BASE = "https://apimetro.dev/"
    private const val GOOGLE_PLACES_BASE = "https://maps.googleapis.com/maps/api/place/"

    const val CLIENT_ID = "AIzaSyBJA5ndVcfeloN4pkyAlS9Q76Ct-CRkGsE"

    val transportApi: TransportApi by lazy {
        Retrofit.Builder()
            .baseUrl(APIMETRO_BASE)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TransportApi::class.java)
    }

    val placeApi: PlaceAPI by lazy {
        Retrofit.Builder()
            .baseUrl(GOOGLE_PLACES_BASE)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlaceAPI::class.java)
    }


}
