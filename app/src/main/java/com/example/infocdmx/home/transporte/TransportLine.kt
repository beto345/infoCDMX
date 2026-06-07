package com.example.infocdmx.home.transporte

data class TransportLine(
    val id: String,
    val name: String,
    val type: String, // "Metro" or "Metrobús"
    val color: String, // Hex color
    val stations: List<String> = emptyList()
)
