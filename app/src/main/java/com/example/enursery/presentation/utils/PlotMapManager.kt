package com.example.enursery.presentation.utils

import android.content.Context
import com.example.enursery.R
import com.example.enursery.core.domain.model.PlotWithVgmCountModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class PlotMapManager(
    private val context: Context,
    private val map: GoogleMap,
    private val onMarkerClicked: (String) -> Unit
) {
    fun setPlotMarker(plot: PlotWithVgmCountModel) {
        val latLng = LatLng(plot.latitude, plot.longitude)
        map.clear()
        val marker = map.addMarker(MarkerOptions().position(latLng).title(plot.namaPlot))
        marker?.tag = plot.idPlot
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
    }

    fun setupMarkerListener() {
        map.setOnMarkerClickListener { marker ->
            (marker.tag as? String)?.let { onMarkerClicked(it) }
            true
        }
    }

    fun toggleTiltView(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(18f)
            .tilt(45f)
            .bearing(0f)
            .build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    fun applyStyle(index: Int) {
        when (index) {
            0 -> map.setMapStyle(null)
            1 -> map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_dark))
            2 -> map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            3 -> map.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }
    }
}
