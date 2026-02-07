package com.example.locationsharing.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        if (hasLocationPermission()) {
            enableLocation()
        } else {
            requestPermission()
        }

        loadMarkers()
    }

    private fun enableLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        map.isMyLocationEnabled = true

        val lat = intent.getDoubleExtra("lat", 23.8103)
        val lng = intent.getDoubleExtra("lng", 90.4125) // Dhaka default

        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(lat, lng),
                15f
            )
        )
    }

    private fun loadMarkers() {
        firestore.collection("users")
            .addSnapshotListener { snap, _ ->
                map.clear()
                snap?.toObjects(User::class.java)?.forEach {
                    if (it.latitude != 0.0 && it.longitude != 0.0) {
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

    private fun hasLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            1001
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            enableLocation()
        }
    }
}
