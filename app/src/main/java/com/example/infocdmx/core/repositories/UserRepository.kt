package com.example.infocdmx.core.repositories

import android.util.Log
import com.example.infocdmx.core.ResponseService
import com.example.infocdmx.onboarding.personal.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepository: UserService {
    private val firestore = FirebaseFirestore.getInstance()
    private val userCollection = firestore.collection("users")

    override suspend fun saveUserInfo(userProfile: UserProfile): ResponseService<Unit> = withContext(Dispatchers.IO) {
        try {
            userCollection.document(userProfile.id)
                .set(userProfile)
                .await()
            ResponseService.Success(Unit)
        } catch (e: Exception) {
            ResponseService.Error("No se pudo crear el perfil: ${e.localizedMessage}")
        }
    }

    override suspend fun getUserInfo(uid: String): ResponseService<UserProfile> = withContext(Dispatchers.IO) {
        try {
            Log.d("UserRepository", "getUserInfo: Fetching info for UID: $uid")
            val document = userCollection.document(uid).get().await()
            Log.d("UserRepository", "getUserInfo: Document exists: ${document.exists()}")
            if (document.exists()) {
                val userProfile = document.toObject(UserProfile::class.java)
                Log.d("UserRepository", "getUserInfo: userProfile = $userProfile")
                if (userProfile != null) {
                    ResponseService.Success(userProfile)
                } else {
                    ResponseService.Error("Error al convertir los datos del usuario")
                }
            } else {
                ResponseService.Error("Usuario no encontrado en la base de datos")
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "getUserInfo: Error", e)
            ResponseService.Error("Error al obtener datos: ${e.localizedMessage}")
        }
    }
}