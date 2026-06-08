package com.example.infocdmx.home.transporte

data class TransportLine(
    val id: String,
    val lineaId: Int,
    val sistemaCode: String,   // "METRO" o "MB"
    val name: String,
    val type: String,          // "Metro" o "Metrobús"
    val numComercial: String,
    val colorHex: String,
    val lengthKm: Double
)
