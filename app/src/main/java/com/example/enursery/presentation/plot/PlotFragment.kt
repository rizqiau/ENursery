package com.example.enursery.presentation.plot

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.enursery.R
import com.example.enursery.core.domain.model.PlotWithVgmCountModel
import com.example.enursery.core.ui.ViewModelFactory
import com.example.enursery.core.utils.CameraBehaviorManager
import com.example.enursery.core.utils.DateFormatter
import com.example.enursery.core.utils.GpsOverlayManager
import com.example.enursery.core.utils.PlotMapManager
import com.example.enursery.core.utils.PlotNavigator
import com.example.enursery.core.utils.UserLocationTracker
import com.example.enursery.databinding.FragmentPlotBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment

class PlotFragment : Fragment() {

    private var _binding: FragmentPlotBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlotViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var googleMap: GoogleMap
    private lateinit var plotMapManager: PlotMapManager
    private lateinit var userLocationTracker: UserLocationTracker
    private lateinit var gpsOverlayManager: GpsOverlayManager
    private lateinit var plotNavigator: PlotNavigator
    private lateinit var cameraBehaviorManager: CameraBehaviorManager

    private var plotList: List<PlotWithVgmCountModel> = emptyList()
    private var currentIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlotBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ibNext.setOnClickListener {
            if (plotList.isNotEmpty()) {
                currentIndex = (currentIndex + 1) % plotList.size
                displayCurrentPlot()
            }
        }

        binding.ibBack.setOnClickListener {
            if (plotList.isNotEmpty()) {
                currentIndex = if (currentIndex - 1 < 0) plotList.size - 1 else currentIndex - 1
                displayCurrentPlot()
            }
        }

        binding.btnStyleMap.setOnClickListener {
            showMapStyleDialog()
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapContainer) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            googleMap = map
            plotMapManager = PlotMapManager(requireContext(), map) { idPlot ->
                plotList.find { it.idPlot == idPlot }?.let { plotNavigator.show(it) }
            }
            userLocationTracker = UserLocationTracker(
                context = requireContext(),
                map = googleMap,
                lifecycleOwner = viewLifecycleOwner,
                onLocationUpdate = { location ->
                    binding.tvAccuracy.text = "Accuracy : ${location.accuracy.toInt()} m"
                    binding.tvLatitude.text = "Latitude : ${location.latitude}"
                    binding.tvLongitude.text = "Longitude : ${location.longitude}"
                }
            )

            gpsOverlayManager = GpsOverlayManager(binding.root)
            plotNavigator = PlotNavigator(requireContext(), layoutInflater)

            cameraBehaviorManager = CameraBehaviorManager(
                context = requireContext(),
                map = googleMap,
                btnGps = binding.btnMyLocation,
                getUserLocation = { userLocationTracker.currentLatLng }
            )

            plotMapManager.setupMarkerListener()
            userLocationTracker.startTracking()
            setupObservers()
        }
    }

    private fun setupObservers() {
        viewModel.plotList.observe(viewLifecycleOwner) { result ->
            result.data?.let {
                plotList = it
                displayCurrentPlot()
            }
        }
    }

    private fun displayCurrentPlot() {
        val plot = plotList[currentIndex]

        binding.plotName.text = plot.namaPlot
        binding.tvLuasArea.text = plot.luasArea.toString()
        binding.tvTanggalTanam.text = DateFormatter.formatTanggalIndonesia(plot.tanggalTanam)            // ✅ langsung
        binding.tvTanggalTransplanting.text = DateFormatter.formatTanggalIndonesia(plot.tanggalTransplantasi) // ✅ langsung
        binding.tvVarietas.text = plot.varietas
        binding.tvJumlahBibit.text = plot.jumlahBibit.toString()
        binding.tvJumlahVgm.text = plot.jumlahVgm.toString()

        plotMapManager.setPlotMarker(plot)
    }

    private fun showMapStyleDialog() {
        val options = arrayOf("Default", "Dark", "Satellite", "Terrain")
        AlertDialog.Builder(requireContext())
            .setTitle("Pilih Style Peta")
            .setItems(options) { _, which ->
                plotMapManager.applyStyle(which)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userLocationTracker.stopTracking()
        _binding = null
    }
}
