package com.example.infocdmx.core.repositories

import com.example.infocdmx.core.ResponseService
import com.example.infocdmx.core.model.Place
import com.example.infocdmx.core.network.ApiClient
import com.example.infocdmx.core.network.PlaceService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaceRepository : PlaceService {
    private val api = ApiClient.placeApi

    override suspend fun getPlace(limit: Int): ResponseService<List<Place>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getPlaces(
                    apiKey = ApiClient.CLIENT_ID,
                    limit = limit
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        ResponseService.Success(body.results)
                    } else {
                        ResponseService.Error("Respuesta vacía del servidor")
                    }
                } else {
                    ResponseService.Error("Error ${response.code()}: ${response.message()}")
                }
            } catch (e: Exception) {
                ResponseService.Error(
                    "No se pudieron cargar los lugares: ${e.localizedMessage}"
                )
            }
        }
}
