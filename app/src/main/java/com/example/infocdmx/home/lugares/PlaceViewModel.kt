package com.example.infocdmx.home.lugares

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infocdmx.core.ResponseService
import com.example.infocdmx.core.model.Place
import com.example.infocdmx.core.network.PlaceService
import com.example.infocdmx.core.repositories.PlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaceViewModel (
    private val service: PlaceService = PlaceRepository()
) : ViewModel() {

    private val _placeState = MutableStateFlow<ResponseService<List<Place>>?>(null)
    val placeState: StateFlow<ResponseService<List<Place>>?> = _placeState.asStateFlow()

    fun loadPlaces(context: Context, limit: Int = 20) {
        viewModelScope.launch {
            _placeState.value = ResponseService.Loading
            _placeState.value = service.getPlace(context, limit)
        }
    }
}
