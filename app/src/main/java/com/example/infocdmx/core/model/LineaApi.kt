package com.example.infocdmx.core.model

import com.google.gson.annotations.SerializedName

data class LineaApi(
    @SerializedName("linea_id") val lineaId: Int = 0,
    @SerializedName("nombre") val nombre: String = "",
    @SerializedName("num_comercial") val numComercial: String? = null,
    @SerializedName("sistema") val sistema: String? = null,
    @SerializedName("color_esp") val colorEsp: String? = null,
    @SerializedName("color_en") val colorEn: String? = null,
    @SerializedName("tam_km") val tamKm: Double = 0.0,
    @SerializedName("existe") val existe: Boolean = true,
    @SerializedName("clasificacion") val clasificacion: String? = null
)