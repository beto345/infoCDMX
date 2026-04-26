package com.example.infocdmx.core

sealed class ResponseService {
    data class Success(val value: Boolean)
    data class Error(val error: String)
}