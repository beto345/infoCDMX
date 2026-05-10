package com.example.infocdmx.onboarding.signin

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infocdmx.core.AuthRepository
import com.example.infocdmx.core.ResponseService
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignViewModel : ViewModel() {

    private val repository = AuthRepository()

    // --- Estados observables (uno por flujo) ---

    private val _signInState = MutableStateFlow<ResponseService<FirebaseUser>?>(null)
    val signInState: StateFlow<ResponseService<FirebaseUser>?> = _signInState.asStateFlow()

    private val _registerState = MutableStateFlow<ResponseService<FirebaseUser>?>(null)
    val registerState: StateFlow<ResponseService<FirebaseUser>?> = _registerState.asStateFlow()

    private val _resetState = MutableStateFlow<ResponseService<Unit>?>(null)
    val resetState: StateFlow<ResponseService<Unit>?> = _resetState.asStateFlow()

    // --- Validaciones de campos ---

    fun validateEmail(email: String): String? {
        if (email.isBlank()) return "El correo es requerido"
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Correo inválido"
        return null
    }

    fun validatePassword(password: String): String? {
        if (password.isBlank()) return "La contraseña es requerida"
        if (password.length < 8) return "Mínimo 8 caracteres"
        return null
    }

    fun validateConfirmPassword(password: String, confirm: String): String? {
        if (confirm.isBlank()) return "Confirma tu contraseña"
        if (password != confirm) return "Las contraseñas no coinciden"
        return null
    }

    fun isLoginFormValid(email: String, password: String): Boolean =
        validateEmail(email) == null && validatePassword(password) == null

    fun isRegisterFormValid(email: String, password: String, confirm: String): Boolean =
        validateEmail(email) == null &&
                validatePassword(password) == null &&
                validateConfirmPassword(password, confirm) == null

    fun isResetFormValid(email: String): Boolean =
        validateEmail(email) == null

    // --- Operaciones ---

    fun requestLogin(email: String, password: String) {
        viewModelScope.launch {
            _signInState.value = ResponseService.Loading
            _signInState.value = repository.requestLogin(email, password)
        }
    }

    fun requestSignUp(email: String, password: String) {
        viewModelScope.launch {
            _registerState.value = ResponseService.Loading
            _registerState.value = repository.requestSignUp(email, password)
        }
    }

    fun requestResetPassword(email: String) {
        viewModelScope.launch {
            _resetState.value = ResponseService.Loading
            _resetState.value = repository.requestResetPassword(email)
        }
    }
}