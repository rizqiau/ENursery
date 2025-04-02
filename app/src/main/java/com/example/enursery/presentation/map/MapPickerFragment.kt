package com.example.enursery.presentation.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.enursery.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapPickerFragment : Fragment(), OnMapReadyCallback {

    private var selectedLatLng: LatLng? = null
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_map_picker, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_picker_view) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        view.findViewById<Button>(R.id.btnSaveLocation).setOnClickListener {
            selectedLatLng?.let {
                val result = Bundle().apply {
                    putDouble("latitude", it.latitude)
                    putDouble("longitude", it.longitude)
                }
                parentFragmentManager.setFragmentResult("lokasi_dipilih", result)
                parentFragmentManager.popBackStack()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap.uiSettings.isZoomControlsEnabled = true

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            val current = LatLng(location.latitude, location.longitude)
            selectedLatLng = current
            val marker = googleMap.addMarker(
                MarkerOptions().position(current)
                    .draggable(true)
                    .title("Geser atau tap map")
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 17f))

            googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                override fun onMarkerDragStart(p0: Marker) {}
                override fun onMarkerDrag(p0: Marker) {}
                override fun onMarkerDragEnd(p0: Marker) {
                    selectedLatLng = p0.position
                }
            })

            // Tambahkan: klik map untuk pindahkan marker
            googleMap.setOnMapClickListener { latLng ->
                marker?.position = latLng
                selectedLatLng = latLng
            }
        }
    }
}
