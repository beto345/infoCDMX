package com.example.infocdmx.core.repositories

import android.content.Context
import com.example.infocdmx.core.ResponseService
import com.example.infocdmx.core.model.Place
import com.example.infocdmx.core.model.PlaceResponse
import com.example.infocdmx.core.network.PlaceService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class PlaceRepository : PlaceService {

    override suspend fun getPlace(context: Context, limit: Int): ResponseService<List<Place>> =
        withContext(Dispatchers.IO) {
            try {
                // Priorizamos el JSON local como solicitado para tener datos "más completos" y con imágenes
                val inputStream = context.assets.open("lugares.json")
                val reader = InputStreamReader(inputStream)
                val type = object : TypeToken<List<Place>>() {}.type
                val places: List<Place> = Gson().fromJson(reader, type)
                reader.close()
                
                if (places.isNotEmpty()) {
                    ResponseService.Success(places.take(limit))
                } else {
                    ResponseService.Error("No se encontraron lugares en el archivo local")
                }
            } catch (e: Exception) {
                ResponseService.Error(
                    "Error al cargar lugares desde el JSON: ${e.localizedMessage}"
                )
            }
        }
}
