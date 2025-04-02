package com.example.enursery.presentation.plot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.enursery.R
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.ui.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class PlotFragment : Fragment() {

    private val viewModel: PlotViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_plot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.plotFragment) as SupportMapFragment?
        mapFragment?.getMapAsync { map ->
            googleMap = map
            observePlotData()
        }
    }

    private fun observePlotData() {
        viewModel.plotList.observe(viewLifecycleOwner) { resource ->
            if (resource is Resource.Success) {
                val plotList = resource.data ?: return@observe

                plotList.forEach { plot ->
                    val lat = plot.latitude
                    val lng = plot.longitude
                    val title = plot.namaPlot

                    if (lat != null && lng != null) {
                        val position = LatLng(lat, lng)
                        googleMap?.addMarker(
                            MarkerOptions()
                                .position(position)
                                .title(title)
                        )
                    }
                }

                // Optional: focus kamera ke salah satu marker pertama
                plotList.firstOrNull()?.let { firstPlot ->
                    firstPlot.latitude?.let { lat ->
                        firstPlot.longitude?.let { lng ->
                            val firstLatLng = LatLng(lat, lng)
                            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 15f))
                        }
                    }
                }
            }
        }
    }
}
