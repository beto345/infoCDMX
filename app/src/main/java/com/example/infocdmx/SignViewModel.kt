package com.example.infocdmx

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infocdmx.core.AuthRepository
import kotlinx.coroutines.launch

class SignViewModel: ViewModel() {
    val repository = AuthRepository()

    fun requestSignUp(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.requestSignUp(email, password)
            result?.let { user ->
                Log.i("Session", "Se ha creado el usuario ${user.uid}")
            } ?: run {
                Log.e("Error", "Hubo un error al crear al usuario")
            }
        }
    }

    fun requestResetPassword(email: String) {
        viewModelScope.launch {
            val success = repository.requestResetPassword(email)
            if (success) {
                Log.i("Session", "Se ha enviado el correo de restablecimiento a $email")
            } else {
                Log.e("Error", "Hubo un error al enviar el correo de restablecimiento")
            }
        }
    }
}