package com.example.infocdmx.core.model

import com.google.gson.annotations.SerializedName

data class EstacionApi(
    @SerializedName("estacion_id") val estacionId: Int = 0,
    @SerializedName("nombre") val nombre: String = "",
    @SerializedName("num_estacion") val numEstacion: Int = 0,
    @SerializedName("linea_id") val lineaId: Int = 0,
    @SerializedName("sistema") val sistema: String? = null,
    @SerializedName("tipo") val tipo: String? = null,
    @SerializedName("alcaldia_municipio") val alcaldiaMunicipio: String? = null,
    @SerializedName("es_cetram") val esCetram: Boolean = false,
    @SerializedName("existe") val existe: Boolean = true
)