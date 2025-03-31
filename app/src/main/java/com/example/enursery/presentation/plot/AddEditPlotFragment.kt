package com.example.enursery.presentation.plot

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.enursery.R
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.domain.model.Plot
import com.example.enursery.core.ui.ViewModelFactory
import com.example.enursery.databinding.FragmentAddPlotBinding
import com.example.enursery.presentation.home.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddEditPlotFragment : Fragment() {

    private var _binding: FragmentAddPlotBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private var mode: String = "ADD"
    private var plotId: String? = null
    private var currentPlot: Plot? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mode = arguments?.getString("mode") ?: "ADD"
        plotId = arguments?.getString("plotId")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        setupDatePickers()
        setupLocationAccess()
        setupMapPickerResult()

        if (mode == "EDIT" && plotId != null) {
            observePlotData()
            binding.btnSimpanPlot.text = "Update Plot"
        } else {
            binding.btnSimpanPlot.text = "Simpan Plot"
        }

        binding.btnSimpanPlot.setOnClickListener {
            if (mode == "EDIT") onUpdatePlotClicked() else onInsertPlotClicked()
        }

        binding.btnAturLokasi.setOnClickListener {
            findNavController().navigate(R.id.mapPickerFragment)
        }
    }

    private fun observePlotData() {
        viewModel.getAllPlots().observe(viewLifecycleOwner) { resource ->
            if (resource is Resource.Success) {
                val found = resource.data?.find { it.idPlot == plotId }
                if (found != null) {
                    currentPlot = found
                    bindPlotToForm(found)
                } else {
                    Toast.makeText(context, "Plot tidak ditemukan", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun bindPlotToForm(plot: Plot) = binding.apply {
        etNamaPlot.setText(plot.namaPlot)
        etLuasArea.setText(plot.luasArea.toString())
        etTanggalTanam.setText(formatter.format(plot.tanggalTanam))
        etTanggalTransplantasi.setText(formatter.format(plot.tanggalTransplantasi))
        etVarietas.setText(plot.varietas)
        etLatitude.setText(plot.latitude.toString())
        etLongitude.setText(plot.longitude.toString())
        etJumlahBibit.setText(plot.jumlahBibit.toString())
    }

    private fun onInsertPlotClicked() {
        val plot = buildPlotFromForm() ?: return
        viewModel.insertSinglePlot(plot)
        Toast.makeText(requireContext(), "Plot berhasil ditambahkan", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    private fun onUpdatePlotClicked() {
        val original = currentPlot ?: return
        val updated = buildPlotFromForm(idOverride = original.idPlot) ?: return
        viewModel.updatePlot(updated)
        Toast.makeText(requireContext(), "Plot berhasil diperbarui", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    private fun buildPlotFromForm(idOverride: String? = null): Plot? {
        val nama = binding.etNamaPlot.text.toString().trim()
        val luas = binding.etLuasArea.text.toString().toDoubleOrNull()
        val tanam = binding.etTanggalTanam.text.toString()
        val trans = binding.etTanggalTransplantasi.text.toString()
        val varietas = binding.etVarietas.text.toString().trim()
        val bibit = binding.etJumlahBibit.text.toString().toIntOrNull()
        val lat = binding.etLatitude.text.toString().toDoubleOrNull()
        val lng = binding.etLongitude.text.toString().toDoubleOrNull()

        if (nama.isBlank() || luas == null || tanam.isBlank() || trans.isBlank() ||
            varietas.isBlank() || bibit == null || lat == null || lng == null
        ) {
            Toast.makeText(requireContext(), "Lengkapi semua data", Toast.LENGTH_SHORT).show()
            return null
        }

        val finalNama = if (nama.startsWith("Plot", true)) nama else "Plot $nama"
        val id = idOverride ?: finalNama.replace(" ", "_")

        return Plot(
            idPlot = id,
            namaPlot = finalNama,
            luasArea = luas,
            tanggalTanam = formatter.parse(tanam) ?: Date(),
            tanggalTransplantasi = formatter.parse(trans) ?: Date(),
            varietas = varietas,
            latitude = lat,
            longitude = lng,
            jumlahBibit = bibit
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
                val date = String.format("%04d-%02d-%02d", year, month + 1, day)
                onDateSelected(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapContainer) as? SupportMapFragment
            ?: return
        mapFragment.getMapAsync { map ->
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
            map.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                    map.addMarker(MarkerOptions().position(latLng).title("Lokasi Anda"))
                    binding.etLatitude.setText(it.latitude.toString())
                    binding.etLongitude.setText(it.longitude.toString())
                }
            }
        }
    }

    private fun setupLocationAccess() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }
    }

    private fun setupMapPickerResult() {
        parentFragmentManager.setFragmentResultListener("lokasi_dipilih", viewLifecycleOwner) { _, bundle ->
            val lat = bundle.getDouble("latitude")
            val lng = bundle.getDouble("longitude")
            binding.etLatitude.setText(lat.toString())
            binding.etLongitude.setText(lng.toString())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 100 && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            Toast.makeText(requireContext(), "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
