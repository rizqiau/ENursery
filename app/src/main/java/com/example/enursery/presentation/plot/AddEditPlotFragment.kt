package com.example.enursery.presentation.plot

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.enursery.R
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.domain.model.Baris
import com.example.enursery.core.domain.model.Plot
import com.example.enursery.core.ui.BarisAdapter
import com.example.enursery.core.ui.ViewModelFactory
import com.example.enursery.core.utils.DateFormatter
import com.example.enursery.core.utils.IdGenerator
import com.example.enursery.core.utils.VgmGenerator
import com.example.enursery.databinding.FragmentAddEditPlotBinding
import com.example.enursery.presentation.home.HomeViewModel
import com.example.enursery.presentation.utils.PlotNameFormatter
import com.example.enursery.presentation.utils.VarietasList
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.time.LocalDate
import java.util.Calendar

class AddEditPlotFragment : Fragment() {

    private var _binding: FragmentAddEditPlotBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var mode: String = "ADD"
    private var plotId: String? = null
    private var currentPlot: Plot? = null

    private var currentMap: GoogleMap? = null
    private var currentMarker: Marker? = null

    private lateinit var barisAdapter: BarisAdapter
    private var barisList: MutableList<Baris> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditPlotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mode = arguments?.getString("mode") ?: "ADD"
        plotId = arguments?.getString("plotId")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        setupDatePickers()
        setupMap()
        setupMapPickerResult()
        setupVarietasDropdown()

        binding.etJumlahBaris.doAfterTextChanged { text ->
            val jumlah = text.toString().toIntOrNull() ?: return@doAfterTextChanged
            val namaInput = binding.etNamaPlot.text.toString().trim()
            if (namaInput.isBlank()) {
                Toast.makeText(requireContext(), "Isi Nama Plot terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@doAfterTextChanged
            }

            val idPlot = PlotNameFormatter.formatId(namaInput)
            barisList = generateBarisList(idPlot, jumlah).toMutableList()

            barisAdapter = BarisAdapter(barisList)
            binding.rvBaris.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = barisAdapter
                visibility = View.VISIBLE
            }

            binding.tvTitleInputBaris.visibility = View.VISIBLE
        }

        if (mode == "EDIT" && plotId != null) {
            observePlot()
        }

        binding.btnSimpanPlot.text = if (mode == "EDIT") "Update Plot" else "Simpan Plot"

        binding.btnSimpanPlot.setOnClickListener {
            if (mode == "EDIT") onUpdatePlotClicked() else onInsertPlotClicked()
        }

        binding.btnAturLokasi.setOnClickListener {
            findNavController().navigate(R.id.mapPickerFragment)
        }
    }

    private fun observePlot() {
        viewModel.getAllPlots().observe(viewLifecycleOwner) { resource ->
            if (resource is Resource.Success) {
                val found = resource.data?.find { it.idPlot == plotId }
                found?.let {
                    currentPlot = it
                    bindPlotToForm(it)
                    updateMarkerLocation(LatLng(it.latitude, it.longitude), "Lokasi Terdaftar")
                } ?: run {
                    Toast.makeText(context, "Plot tidak ditemukan", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun bindPlotToForm(plot: Plot) = binding.apply {
        etNamaPlot.setText(plot.namaPlot)
        etLuasArea.setText(plot.luasArea.toString())
        etTanggalTanam.setText(DateFormatter.formatToInput(DateFormatter.toLocalDate(plot.tanggalTanam)))
        etTanggalTransplantasi.setText(DateFormatter.formatToInput(DateFormatter.toLocalDate(plot.tanggalTransplantasi)))
        binding.etVarietas.setText(plot.varietas, false)
        etLatitude.setText(plot.latitude.toString())
        etLongitude.setText(plot.longitude.toString())
        etJumlahBibit.setText(plot.jumlahBibit.toString())
    }

    private fun onInsertPlotClicked() {
        val plot = buildPlotFromForm() ?: return
        val updatedBarisList = barisAdapter.getUpdatedBarisList().map {
            it.copy(idPlot = plot.idPlot, idBaris = IdGenerator.generateBarisId(plot.idPlot, it.namaBaris))
        }
        val vgmList = VgmGenerator.generateFromBarisList(plot.idPlot, updatedBarisList)

        viewModel.insertSinglePlot(plot, updatedBarisList, vgmList)

        // Observe LiveData hasil insert
        viewModel.insertResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Plot berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Gagal simpan plot: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onUpdatePlotClicked() {
        val plot = buildPlotFromForm(currentPlot?.idPlot) ?: return
        val updatedBarisList = barisAdapter.getUpdatedBarisList().map {
            it.copy(idPlot = plot.idPlot, idBaris = IdGenerator.generateBarisId(plot.idPlot, it.namaBaris))
        }
        viewModel.updatePlot(plot, updatedBarisList)
        Toast.makeText(requireContext(), "Plot berhasil diperbarui", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    private fun setupVarietasDropdown() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, VarietasList.data)
        binding.etVarietas.setAdapter(adapter)
    }

    private fun buildPlotFromForm(idOverride: String? = null): Plot? {
        val namaInput = binding.etNamaPlot.text.toString().trim()
        val luas = binding.etLuasArea.text.toString().toDoubleOrNull()
        val tanam = binding.etTanggalTanam.text.toString()
        val trans = binding.etTanggalTransplantasi.text.toString()
        val varietas = binding.etVarietas.text.toString().trim()

        if (varietas !in VarietasList.data) {
            Toast.makeText(requireContext(), "Pilih varietas dari daftar", Toast.LENGTH_SHORT).show()
            return null
        }

        val bibit = binding.etJumlahBibit.text.toString().toIntOrNull()
        val lat = binding.etLatitude.text.toString().toDoubleOrNull()
        val lng = binding.etLongitude.text.toString().toDoubleOrNull()

        val tanggalTanamParsed = DateFormatter.parseInputTanggal(tanam)
        val tanggalTransParsed = DateFormatter.parseInputTanggal(trans)

        if (listOf(namaInput, tanam, trans, varietas).any { it.isBlank() } ||
            listOf(luas, bibit, lat, lng, tanggalTanamParsed, tanggalTransParsed).any { it == null }) {
            Toast.makeText(requireContext(), "Lengkapi semua data", Toast.LENGTH_SHORT).show()
            return null
        }

        val tanggalTanam = DateFormatter.toEpochDay(tanggalTanamParsed!!)
        val tanggalTrans = DateFormatter.toEpochDay(tanggalTransParsed!!)
        val finalNama = PlotNameFormatter.formatName(namaInput)
        val id = idOverride ?: PlotNameFormatter.formatId(namaInput)

        return Plot(
            idPlot = id,
            namaPlot = finalNama,
            luasArea = luas!!,
            tanggalTanam = tanggalTanam,
            tanggalTransplantasi = tanggalTrans,
            varietas = varietas,
            latitude = lat!!,
            longitude = lng!!,
            jumlahBibit = bibit!!
        )
    }

    private fun setupDatePickers() {
        binding.etTanggalTanam.setOnClickListener {
            showDatePicker { binding.etTanggalTanam.setText(it) }
        }
        binding.etTanggalTransplantasi.setOnClickListener {
            showDatePicker { binding.etTanggalTransplantasi.setText(it) }
        }
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val date = LocalDate.of(year, month + 1, day)
                onDateSelected(DateFormatter.formatToInput(date))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapContainer) as? SupportMapFragment ?: return
        mapFragment.getMapAsync { map ->
            currentMap = map
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true

            val lat = binding.etLatitude.text.toString().toDoubleOrNull()
            val lng = binding.etLongitude.text.toString().toDoubleOrNull()

            if (lat != null && lng != null) {
                updateMarkerLocation(LatLng(lat, lng), "Lokasi Terdaftar")
            } else {
                fetchGpsAndMoveMarker()
            }
        }
    }

    private fun generateBarisList(idPlot: String, jumlah: Int): List<Baris> {
        return (1..jumlah).map { index ->
            val namaBaris = index.toString()
            val idBaris = IdGenerator.generateBarisId(idPlot, namaBaris)
            Baris(
                idBaris = idBaris,
                idPlot = idPlot,
                namaBaris = namaBaris,
                jumlahTargetVgm = 0
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchGpsAndMoveMarker() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            currentMap?.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    updateMarkerLocation(latLng, "Lokasi Anda")
                    if (binding.etLatitude.text.isNullOrEmpty()) binding.etLatitude.setText(it.latitude.toString())
                    if (binding.etLongitude.text.isNullOrEmpty()) binding.etLongitude.setText(it.longitude.toString())
                }
            }
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }
    }

    private fun updateMarkerLocation(latLng: LatLng, title: String = "Lokasi Terpilih") {
        currentMarker?.remove()
        currentMarker = currentMap?.addMarker(MarkerOptions().position(latLng).title(title))
        currentMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
    }

    private fun setupMapPickerResult() {
        parentFragmentManager.setFragmentResultListener("lokasi_dipilih", viewLifecycleOwner) { _, bundle ->
            val lat = bundle.getDouble("latitude")
            val lng = bundle.getDouble("longitude")
            binding.etLatitude.setText(lat.toString())
            binding.etLongitude.setText(lng.toString())
            updateMarkerLocation(LatLng(lat, lng))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 100 && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            fetchGpsAndMoveMarker()
        } else {
            Toast.makeText(requireContext(), "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
