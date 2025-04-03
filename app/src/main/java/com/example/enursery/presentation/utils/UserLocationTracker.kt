package com.example.enursery.presentation.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

class UserLocationTracker(
    private val context: Context,
    private val map: GoogleMap,
    private val lifecycleOwner: LifecycleOwner,
    private val onLocationUpdate: (Location) -> Unit
) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null
    var currentLatLng: LatLng? = null
        private set

    @SuppressLint("MissingPermission")
    fun startTracking() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) return

        map.isMyLocationEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = false
        map.uiSettings.isCompassEnabled = false

        // Lokasi terakhir
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                currentLatLng = LatLng(it.latitude, it.longitude)
                onLocationUpdate(it)
            }
        }

        val request = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation ?: return
                currentLatLng = LatLng(location.latitude, location.longitude)
                onLocationUpdate(location)
            }
        }

        fusedLocationClient.requestLocationUpdates(
            request,
            locationCallback!!,
            Looper.getMainLooper()
        )
    }

    fun stopTracking() {
        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
    }
}

