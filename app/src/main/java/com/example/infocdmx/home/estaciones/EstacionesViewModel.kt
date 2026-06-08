package com.example.infocdmx.home.estaciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infocdmx.core.ResponseService
import com.example.infocdmx.core.repositories.TransportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EstacionesViewModel(
    private val repository: TransportRepository = TransportRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<ResponseService<List<TransportStation>>>(ResponseService.Loading)
    val state = _state.asStateFlow()

    fun cargar(sistema: String, lineaId: Int) {
        viewModelScope.launch {
            _state.value = ResponseService.Loading
            try {
                println("CARGAR ESTACIONES: sistema=$sistema, lineaId=$lineaId")
                val estaciones = repository.getEstaciones(sistema, lineaId)
                println("CARGAR ESTACIONES RESULT: ${estaciones.size} estaciones")
                if (estaciones.isEmpty()) {
                    _state.value = ResponseService.Error("No se encontraron estaciones")
                } else {
                    _state.value = ResponseService.Success(estaciones)
                }
            } catch (e: Exception) {
                _state.value = ResponseService.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
