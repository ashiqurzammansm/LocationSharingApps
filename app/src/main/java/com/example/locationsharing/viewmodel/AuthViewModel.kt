package com.example.locationsharing.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.locationsharing.data.model.User
import com.example.locationsharing.data.repository.UserRepository

class AuthViewModel : ViewModel() {

    private val repository = UserRepository()

    val authSuccess: LiveData<Boolean> = repository.authSuccess
    val errorMessage: LiveData<String?> = repository.errorMessage

    fun register(email: String, password: String) {
        repository.registerUser(email, password)
    }

    fun login(email: String, password: String) {
        repository.loginUser(email, password)
    }

    fun saveUserData(
        displayName: String,
        email: String,
        latitude: Double,
        longitude: Double
    ) {
        val uid = repository.getCurrentUserId() ?: return
        val user = User(uid, displayName, email, latitude, longitude)
        repository.saveUser(user)
    }
}