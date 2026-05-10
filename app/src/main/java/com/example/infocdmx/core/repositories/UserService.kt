package com.example.infocdmx.core.repositories

import com.example.infocdmx.core.ResponseService
import com.example.infocdmx.onboarding.personal.model.UserProfile

interface UserService {

    suspend fun saveUserInfo(userProfile: UserProfile): ResponseService<Unit>
}