package com.example.locationsharing.utils

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LocationHelper(context: Context) {

    private val client = LocationServices.getFusedLocationProviderClient(context)
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val request = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, 5000
    ).build()

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        client.requestLocationUpdates(request, callback, null)
    }

    private val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val loc = result.lastLocation ?: return
            val uid = auth.currentUser?.uid ?: return

            firestore.collection("users")
                .document(uid)
                .update(
                    "latitude", loc.latitude,
                    "longitude", loc.longitude
                )
        }
    }
}
