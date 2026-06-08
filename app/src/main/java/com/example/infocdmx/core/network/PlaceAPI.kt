package com.example.infocdmx.core.network

import com.example.infocdmx.core.model.PlaceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceAPI {
    @GET("textsearch/json")
    suspend fun getPlaces(
        @Query("key") apiKey: String,
        @Query("query") query: String = "lugares turisticos cdmx",
        @Query("language") language: String = "es",
        @Query("type") type: String = "tourist_attraction",
        @Query("limit") limit: Int = 20
    ): Response <PlaceResponse>
}
