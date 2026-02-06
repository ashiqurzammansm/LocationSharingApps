package com.example.locationsharing.data.model

data class User(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)