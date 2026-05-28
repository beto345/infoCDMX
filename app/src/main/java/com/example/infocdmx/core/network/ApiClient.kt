package com.example.infocdmx.core.network
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://ea96fae63c8b40fbb067d28d74ed92b8.api.mockbin.io/"
    const val CLIENT_ID = "96fae63c8b40fbb067d28d74ed92b8"
    private val loggin = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggin)
        .build()

    val placeApi: PlaceAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlaceAPI::class.java)
    }
}

