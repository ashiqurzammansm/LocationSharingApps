package com.example.locationsharing.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _updateStatus = MutableLiveData<Boolean>()
    val updateStatus: LiveData<Boolean> = _updateStatus

    fun updateProfile(name: String, lat: Double, lng: Double) {
        val uid = auth.currentUser?.uid ?: return

        val updates = mapOf(
            "displayName" to name,
            "latitude" to lat,
            "longitude" to lng
        )

        firestore.collection("users")
            .document(uid)
            .set(updates, com.google.firebase.firestore.SetOptions.merge())
            .addOnSuccessListener { _updateStatus.postValue(true) }
            .addOnFailureListener { _updateStatus.postValue(false) }
    }
}
