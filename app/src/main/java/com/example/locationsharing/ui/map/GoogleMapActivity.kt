package com.example.locationsharing.ui.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.locationsharing.R
import com.example.locationsharing.data.model.User
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore

class GoogleMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_map)

        val fragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        fragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.isMyLocationEnabled = true
        loadMarkers()

        intent.extras?.let {
            val lat = it.getDouble("lat", 0.0)
            val lng = it.getDouble("lng", 0.0)
            if (lat != 0.0 && lng != 0.0) {
                map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 16f)
                )
            }
        }
    }

    private fun loadMarkers() {
        firestore.collection("users")
            .addSnapshotListener { snap, _ ->
                map.clear()
                snap?.toObjects(User::class.java)?.forEach {
                    if (it.latitude != 0.0) {
                        map.addMarker(
                            MarkerOptions()
                                .position(LatLng(it.latitude, it.longitude))
                                .title(it.displayName)
                                .snippet(it.email)
                        )
                    }
                }
            }
    }
}
