package com.example.locationsharing.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.locationsharing.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    val authSuccess = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String?>()

    fun registerUser(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            errorMessage.postValue("Email & password required")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val uid = auth.currentUser!!.uid
                val user = User(uid, email.substringBefore("@"), email, 0.0, 0.0)
                saveUser(user)
                authSuccess.postValue(true)
            }
            .addOnFailureListener {
                errorMessage.postValue(it.message)
            }
    }

    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authSuccess.postValue(true) }
            .addOnFailureListener { errorMessage.postValue(it.message) }
    }

    fun saveUser(user: User) {
        firestore.collection("users")
            .document(user.uid)
            .set(user)
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid
}
