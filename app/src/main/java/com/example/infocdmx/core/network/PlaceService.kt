package com.example.infocdmx.core.network

import com.example.infocdmx.core.ResponseService
import com.example.infocdmx.core.model.Place

interface PlaceService {
    suspend fun getPlace(limit: Int = 20): ResponseService<List<Place>>
}
