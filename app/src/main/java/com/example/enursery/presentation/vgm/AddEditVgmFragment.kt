package com.example.enursery.presentation.vgm

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.enursery.core.ui.ViewModelFactory
import com.example.enursery.core.utils.IdParser
import com.example.enursery.databinding.FragmentAddEditVgmBinding
import com.example.enursery.presentation.batch.AddBatchBottomSheet
import kotlinx.coroutines.launch
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
        if (isGranted) openCamera()
        else Toast.makeText(requireContext(), "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this, ViewModelFactory.getInstance(requireContext())
        )[VgmViewModel::class.java]

        scannedIdBibit = arguments?.getString("idBibit")
        if (scannedIdBibit.isNullOrBlank()) {
            Toast.makeText(requireContext(), "ID Bibit tidak ditemukan", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        lifecycleScope.launch {
            val isValid = viewModel.isBibitExist(scannedIdBibit!!)
            if (!isValid) {
                Toast.makeText(requireContext(), "ID Bibit tidak valid atau belum terdaftar", Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
                return@launch
            }

            // kalau valid → lanjutkan isi data
            binding.tvIdBibit.setText(scannedIdBibit)
            Log.d("DEBUG_IDBIBIT", "Scanned ID: $scannedIdBibit")
            binding.tvNamaPlot.setText(IdParser.extractIdPlot(scannedIdBibit!!))
            binding.tvNamaBaris.setText(IdParser.extractNamaBaris(scannedIdBibit!!))
        }

        binding.btnUploadFoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
            ) openCamera()
            else requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        binding.btnSubmit.setOnClickListener { handleSubmit() }

        setupStatusDropdown()
        observeDropdownData()
        observeLoadingState()
    }

    private fun setupStatusDropdown() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            VgmStatus.values().map { it.label }
        )
        binding.etStatus.setAdapter(adapter)
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

    private fun handleSubmit() {
        val tinggi = binding.etTinggi.text.toString().toDoubleOrNull() ?: 0.0
        val diameter = binding.etDiameter.text.toString().toDoubleOrNull() ?: 0.0
        val jumlahDaun = binding.etJumlahDaun.text.toString().toIntOrNull() ?: 0
        val lebar = binding.etLebarPetiole.text.toString().toDoubleOrNull() ?: 0.0
        val foto = selectedFotoUri?.toString() ?: ""
        val idBibit = scannedIdBibit ?: return

        val selectedStatusIndex = VgmStatus.values().indexOfFirst { it.label == binding.etStatus.text.toString() }
        val statusEnum = if (selectedStatusIndex >= 0) VgmStatus.values()[selectedStatusIndex] else VgmStatus.AKTIF
        val status = statusEnum.name

        val idPlot = IdParser.extractIdPlot(idBibit)
        val idBaris = IdParser.extractIdBaris(idBibit)
        val selectedBatch = viewModel.currentBatch.getOrNull(viewModel.currentBatch.indexOfFirst {
            it.namaBatch == binding.etBatch.text.toString()
        })
        val idUser = viewModel.getCurrentUserId()
        val namaUser = viewModel.getCurrentUserName()

        if (idPlot == null || idBaris == null || selectedBatch == null || idUser == null || namaUser == null) {
            Toast.makeText(requireContext(), "Data tidak lengkap atau format ID Bibit salah", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.insertVgmHistory(
            idBibit = idBibit,
            idPlot = idPlot,
            idBaris = idBaris,
            idUser = idUser,
            namaUser = namaUser,
            idBatch = selectedBatch.idBatch,
            status = status,
            tinggi = tinggi,
            diameter = diameter,
            jumlahDaun = jumlahDaun,
            lebar = lebar,
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

    private fun observeDropdownData() {
        viewModel.batchList.observe(viewLifecycleOwner) {
            viewModel.currentBatch = it

            val listNamaBatch = it.map { batch -> batch.namaBatch }
            val listWithAdd = listNamaBatch + "Tambah Batch"
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, listWithAdd)
            binding.etBatch.setAdapter(adapter)

            binding.etBatch.setOnItemClickListener { _, _, position, _ ->
                if (position == listWithAdd.lastIndex) {
                    showAddBatchBottomSheet()
                }
            }
        }
    }

    private fun showAddBatchBottomSheet() {
        val allowedRoles = listOf("Supervisor", "Asisten Kebun")
        val role = viewModel.getCurrentUserRole()
        if (role !in allowedRoles) {
            Toast.makeText(requireContext(), "Kamu tidak memiliki akses untuk menambahkan batch", Toast.LENGTH_SHORT).show()
            return
        }

        val dialog = AddBatchBottomSheet()
        dialog.onBatchAdded = { batch ->
            viewModel.insertBatch(batch) { result ->
                if (result.isSuccess) {
                    // Setelah batch ditambahkan, update spinner dan set value-nya
                    viewModel.batchList.observe(viewLifecycleOwner) { updatedList ->
                        viewModel.currentBatch = updatedList
                        binding.etBatch.setText(batch.namaBatch, false)
                    }
                    Toast.makeText(requireContext(), "Batch ditambahkan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Gagal menambahkan batch", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show(parentFragmentManager, "AddBatchBottomSheet")
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

    enum class VgmStatus(val label: String) {
        AKTIF("Aktif"),
        MATI("Mati");

        override fun toString(): String = label
    }
}