package com.example.infocdmx.core.network

import com.example.infocdmx.core.model.EstacionApi
import com.example.infocdmx.core.model.LineaApi
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TransportApi {
    @GET("movilidad/{sistema}/linea")
    suspend fun getLineas(
        @Path("sistema") sistema: String,
        @Query("existe") existe: Boolean = true
    ): List<LineaApi>

    // GET https://apimetro.dev/movilidad/METRO/estacion?linea_id=191&existe=true
    @GET("movilidad/{sistema}/estacion")
    suspend fun getEstaciones(
        @Path("sistema") sistema: String,
        @Query("linea_id") lineaId: Int,
        @Query("existe") existe: Boolean = true
    ): List<EstacionApi>
}