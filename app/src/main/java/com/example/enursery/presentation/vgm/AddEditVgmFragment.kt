package com.example.enursery.presentation.vgm

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.ui.ViewModelFactory
import com.example.enursery.databinding.FragmentAddEditVgmBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddEditVgmFragment : Fragment() {

    private var _binding: FragmentAddEditVgmBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: VgmViewModel
    private var scannedIdBibit: String? = null
    private var selectedFotoUri: Uri? = null
    private var tempCameraUri: Uri? = null

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(requireContext(), "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            val idBibit = scannedIdBibit ?: return@registerForActivityResult
            compressAndSavePhoto(idBibit, tempCameraUri!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditVgmBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireContext())
        )[VgmViewModel::class.java]

        scannedIdBibit = arguments?.getString("idBibit")
        if (scannedIdBibit.isNullOrBlank()) {
            Toast.makeText(requireContext(), "ID Bibit tidak ditemukan", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        binding.tvIdBibit.text = scannedIdBibit

        binding.btnUploadFoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera()
            } else {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }


        binding.btnSubmit.setOnClickListener { handleSubmit() }

        observeSpinnerData()
        observeLoadingState()
    }

    private fun openCamera() {
        val photoFile = File.createTempFile("foto_bibit_", ".jpg", requireContext().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        tempCameraUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            photoFile
        )

        cameraLauncher.launch(tempCameraUri)
    }

    private fun compressAndSavePhoto(idBibit: String, uri: Uri) {
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        val formatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale("id", "ID"))
        val timestamp = formatter.format(Date())
        val fileName = "${idBibit}_$timestamp.jpg"
        val finalUri = saveImageToMediaStore(requireContext(), bitmap, fileName)

        finalUri?.let {
            selectedFotoUri = Uri.parse(it)
            binding.imgBibit.setImageURI(selectedFotoUri)
        }
    }

    private fun saveImageToMediaStore(context: Context, bitmap: Bitmap, fileName: String): String? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/eNursery")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            resolver.openOutputStream(uri)?.use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, output)
            }
            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
            return uri.toString()
        }

        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleSubmit() {
        val tinggi = binding.etTinggi.text.toString().toDoubleOrNull() ?: 0.0
        val diameter = binding.etDiameter.text.toString().toDoubleOrNull() ?: 0.0
        val jumlahDaun = binding.etJumlahDaun.text.toString().toIntOrNull() ?: 0
        val foto = selectedFotoUri?.toString() ?: ""
        val idBibit = scannedIdBibit ?: return

        val selectedPlot = viewModel.currentPlot.getOrNull(binding.spinnerPlot.selectedItemPosition)
        val selectedBatch = viewModel.currentBatch.getOrNull(binding.spinnerBatch.selectedItemPosition)
        val idUser = viewModel.getCurrentUserId()

        if (selectedPlot == null || selectedBatch == null || idUser == null || idBibit.isBlank()) {
            Toast.makeText(requireContext(), "Pastikan semua data telah terisi", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.insertVgmHistory(
            idBibit = idBibit,
            idPlot = selectedPlot.idPlot,
            idUser = idUser,
            idBatch = selectedBatch.idBatch,
            tinggi = tinggi,
            diameter = diameter,
            jumlahDaun = jumlahDaun,
            fotoPath = foto
        ) { result ->
            if (result.isSuccess) {
                Toast.makeText(requireContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeSpinnerData() {
        viewModel.batchList.observe(viewLifecycleOwner) {
            viewModel.currentBatch = it
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                it.map { batch -> batch.namaBatch }
            )
            binding.spinnerBatch.adapter = adapter
        }

        viewModel.plotList.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val list = resource.data ?: emptyList()
                    viewModel.currentPlot = list
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        list.map { plot -> plot.namaPlot }
                    )
                    binding.spinnerPlot.adapter = adapter
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Gagal memuat plot", Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }
        }
    }

    private fun observeLoadingState() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.btnSubmit.isEnabled = !isLoading
            binding.btnSubmit.text = if (isLoading) "Menyimpan..." else "Simpan Data VGM"
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

