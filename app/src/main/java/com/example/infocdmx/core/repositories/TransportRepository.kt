package com.example.infocdmx.core.repositories

import com.example.infocdmx.core.model.EstacionApi
import com.example.infocdmx.core.model.LineaApi
import com.example.infocdmx.core.network.ApiClient
import com.example.infocdmx.core.network.TransportApi
import com.example.infocdmx.home.estaciones.TransportStation
import com.example.infocdmx.home.transporte.TransportLine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TransportRepository(
    private val api: TransportApi = ApiClient.transportApi
) {
    suspend fun getMetroYMetrobus(): List<TransportLine> = withContext(Dispatchers.IO) {
        val metro = runCatching { api.getLineas("METRO", true) }.getOrDefault(emptyList())
        val mb = runCatching { api.getLineas("MB", true) }.getOrDefault(emptyList())
        (metro.map { it.toUi("Metro", "METRO") } + mb.map { it.toUi("Metrobús", "MB") })
            .sortedWith(compareBy({ it.type }, { it.numComercial }))
    }

    suspend fun getEstaciones(sistema: String, lineaId: Int): List<TransportStation> =
        withContext(Dispatchers.IO) {
            val raw = runCatching { api.getEstaciones(sistema, lineaId, true) }
                .getOrDefault(emptyList())
            raw.map { it.toUi() }
                .sortedBy { it.orden }
                .distinctBy { it.nombre }
        }

    private fun LineaApi.toUi(tipo: String, codigo: String) = TransportLine(
        id = lineaId.toString(),
        lineaId = lineaId,
        sistemaCode = codigo,
        name = nombre.ifBlank { "$tipo ${numComercial ?: ""}".trim() },
        type = tipo,
        numComercial = numComercial ?: "",
        colorHex = normalizeHex(colorEsp),
        lengthKm = tamKm
    )

    private fun EstacionApi.toUi() = TransportStation(
        id = estacionId,
        nombre = nombre,
        orden = if (numEstacion > 0) numEstacion else Int.MAX_VALUE,
        tipo = tipo ?: "",
        alcaldia = alcaldiaMunicipio ?: "",
        esCetram = esCetram
    )

    private fun normalizeHex(value: String?): String {
        val v = (value ?: "").trim()
        if (v.isEmpty()) return "#1F4E79"
        return if (v.startsWith("#")) v else "#$v"
    }
}