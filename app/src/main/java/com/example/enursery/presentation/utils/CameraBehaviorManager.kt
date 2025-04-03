package com.example.enursery.presentation.utils

import android.content.Context
import android.widget.ImageButton
import com.example.enursery.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

class CameraBehaviorManager(
    private val context: Context,
    private val map: GoogleMap,
    private val btnGps: ImageButton,
    private val getUserLocation: () -> LatLng?
) {

    private var gpsState = GpsButtonState.GPS_OFF
    private var currentState = GpsButtonState.GPS_OFF

    init {
        btnGps.setOnClickListener {
            handleGpsButtonState()
        }
        observeCameraAndUpdateGpsIcon()
    }

    private fun handleGpsButtonState() {
        when (gpsState) {
            GpsButtonState.GPS_OFF -> {
                centerMapToMyLocation()
                gpsState = GpsButtonState.GPS_ON
            }
            GpsButtonState.GPS_ON -> {
                tiltCameraTo3D()
                gpsState = GpsButtonState.COMPASS_MODE
            }
            GpsButtonState.COMPASS_MODE -> {
                resetCameraTilt()
                gpsState = GpsButtonState.GPS_ON
            }
        }
        updateGpsButtonIcon(gpsState)
    }

    private fun observeCameraAndUpdateGpsIcon() {
        map.setOnCameraIdleListener {
            val userLatLng = getUserLocation() ?: return@setOnCameraIdleListener
            val projection = map.projection
            val visibleRegion = projection.visibleRegion.latLngBounds

            if (visibleRegion.contains(userLatLng)) {
                if (currentState == GpsButtonState.GPS_OFF) {
                    updateGpsButtonIcon(GpsButtonState.GPS_ON)
                }
            } else {
                if (currentState != GpsButtonState.GPS_OFF) {
                    updateGpsButtonIcon(GpsButtonState.GPS_OFF)
                }
            }
        }
    }

    private fun centerMapToMyLocation() {
        getUserLocation()?.let { latLng ->
            map.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder()
                        .target(latLng)
                        .zoom(18f)
                        .tilt(0f)
                        .build()
                )
            )
        }
    }

    private fun tiltCameraTo3D() {
        getUserLocation()?.let { latLng ->
            map.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder()
                        .target(latLng)
                        .zoom(18f)
                        .tilt(45f)
                        .build()
                )
            )
        }
    }

    private fun resetCameraTilt() {
        val current = map.cameraPosition
        map.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder(current)
                    .tilt(0f)
                    .build()
            )
        )
    }

    private fun updateGpsButtonIcon(state: GpsButtonState) {
        currentState = state
        gpsState = state // <-- sinkronisasi status internal
        when (state) {
            GpsButtonState.GPS_OFF -> btnGps.setImageResource(R.drawable.ic_gps_off)
            GpsButtonState.GPS_ON -> btnGps.setImageResource(R.drawable.ic_gps_on)
            GpsButtonState.COMPASS_MODE -> btnGps.setImageResource(R.drawable.ic_compass)
        }
    }
}

enum class GpsButtonState {
    GPS_OFF, GPS_ON, COMPASS_MODE
}

