package com.example.enursery.presentation.utils

import android.location.Location
import android.view.View
import android.widget.TextView
import com.example.enursery.R

class GpsOverlayManager(private val view: View) {
    fun update(location: Location) {
        view.findViewById<TextView>(R.id.tvAccuracy).text = "Accuracy : ${location.accuracy.toInt()} meter"
        view.findViewById<TextView>(R.id.tvLatitude).text = "Latitude : ${location.latitude}"
        view.findViewById<TextView>(R.id.tvLongitude).text = "Longitude : ${location.longitude}"
    }
}
