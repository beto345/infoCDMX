package com.example.infocdmx.home.transporte

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infocdmx.core.ResponseService
import com.example.infocdmx.core.repositories.TransportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransporteViewModel(
    private val repository: TransportRepository = TransportRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<ResponseService<List<TransportLine>>>(ResponseService.Loading)
    val state = _state.asStateFlow()

    fun cargarLineas() {
        viewModelScope.launch {
            _state.value = ResponseService.Loading
            try {
                val lineas = repository.getMetroYMetrobus()
                if (lineas.isEmpty()) {
                    _state.value = ResponseService.Error("No se encontraron líneas de transporte")
                } else {
                    _state.value = ResponseService.Success(lineas)
                }
            } catch (e: Exception) {
                _state.value = ResponseService.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
