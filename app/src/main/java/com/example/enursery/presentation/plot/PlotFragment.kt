package com.example.enursery.presentation.plot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.enursery.R
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.domain.model.PlotWithVgmCountModel
import com.example.enursery.core.ui.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class PlotFragment : Fragment() {

    private val viewModel: PlotViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var googleMap: GoogleMap
    private var plotList: List<PlotWithVgmCountModel> = emptyList()
    private var currentIndex = 0

    private lateinit var tvPlotName: TextView
    private lateinit var tvLuasArea: TextView
    private lateinit var ibNext: ImageButton
    private lateinit var ibBack: ImageButton
    private lateinit var tvTanggalTanam: TextView
    private lateinit var tvTanggalTransplanting: TextView
    private lateinit var tvVarietas: TextView
    private lateinit var tvJumlahBibit: TextView
    private lateinit var tvJumlahVgm: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_plot, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi view
        tvPlotName = view.findViewById(R.id.plotName)
        tvLuasArea = view.findViewById(R.id.tvLuasArea)
        ibNext = view.findViewById(R.id.ibNext)
        ibBack = view.findViewById(R.id.ibBack)
        tvTanggalTanam = view.findViewById(R.id.tvTanggalTanam)
        tvTanggalTransplanting = view.findViewById(R.id.tvTanggalTransplanting)
        tvVarietas = view.findViewById(R.id.tvVarietas)
        tvJumlahBibit = view.findViewById(R.id.tvJumlahBibit)
        tvJumlahVgm = view.findViewById(R.id.tvJumlahVgm)

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapContainer) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            googleMap = map
            setupObserver()
            setupClickListeners()
            setupMarkerClickListener()
        }
    }

    private fun setupObserver() {
        viewModel.plotList.observe(viewLifecycleOwner) { result ->
            if (result is Resource.Success) {
                result.data?.let { data ->
                    plotList = data
                    if (plotList.isNotEmpty()) {
                        currentIndex = 0
                        displayCurrentPlot()
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        ibNext.setOnClickListener {
            if (plotList.isNotEmpty()) {
                currentIndex = (currentIndex + 1) % plotList.size
                displayCurrentPlot()
            }
        }

        ibBack.setOnClickListener {
            if (plotList.isNotEmpty()) {
                currentIndex = if (currentIndex - 1 < 0) plotList.size - 1 else currentIndex - 1
                displayCurrentPlot()
            }
        }
    }

    private fun setupMarkerClickListener() {
        googleMap.setOnMarkerClickListener { marker ->
            val idPlot = marker.tag as? String
            val selected = plotList.find { it.idPlot == idPlot }
            selected?.let {
                currentIndex = plotList.indexOf(it)
                displayCurrentPlot()
            }
            true
        }
    }

    private fun displayCurrentPlot() {
        val plot = plotList[currentIndex]

        tvPlotName.text = plot.namaPlot
        tvLuasArea.text = plot.luasArea.toString()
        tvTanggalTanam.text = plot.tanggalTanam.formatToIndo()
        tvTanggalTransplanting.text = plot.tanggalTransplantasi.formatToIndo()
        tvVarietas.text = plot.varietas
        tvJumlahBibit.text = plot.jumlahBibit.toString()
        tvJumlahVgm.text = plot.jumlahVgm.toString()

        val latLng = LatLng(plot.latitude, plot.longitude)
        googleMap.clear()
        val marker = googleMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(plot.namaPlot)
        )
        marker?.tag = plot.idPlot
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
    }

    private fun LocalDate.formatToIndo(): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale("id", "ID"))
        return this.format(formatter)
    }
}
