package com.example.infocdmx.home.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infocdmx.core.ResponseService
import com.example.infocdmx.core.repositories.UserRepository
import com.example.infocdmx.onboarding.personal.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _userState = MutableStateFlow<ResponseService<UserProfile>?>(null)
    val userState: StateFlow<ResponseService<UserProfile>?> = _userState.asStateFlow()

    fun fetchUserInfo(uid: String) {
        viewModelScope.launch {
            _userState.value = ResponseService.Loading
            _userState.value = repository.getUserInfo(uid)
        }
    }
}