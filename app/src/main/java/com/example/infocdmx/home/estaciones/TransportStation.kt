package com.example.infocdmx.home.estaciones

data class TransportStation(
    val id: Int,
    val nombre: String,
    val orden: Int,
    val tipo: String,
    val alcaldia: String,
    val esCetram: Boolean
)