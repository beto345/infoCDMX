package com.example.infocdmx.core.network

import android.content.Context
import com.example.infocdmx.core.ResponseService
import com.example.infocdmx.core.model.Place

interface PlaceService {
    suspend fun getPlace(context: Context, limit: Int = 20): ResponseService<List<Place>>
}
