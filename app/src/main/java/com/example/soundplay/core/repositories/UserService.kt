package com.example.soundplay.core.repositories

import com.example.soundplay.core.ResponseService
import com.example.soundplay.onboarding.personal.model.UserProfile

interface UserService {
    suspend fun saveUserInfo(userProfile: UserProfile): ResponseService<Unit>
}