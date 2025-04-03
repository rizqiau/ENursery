package com.example.enursery.presentation.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.enursery.R
import com.example.enursery.core.ui.ViewModelFactory
import com.example.enursery.databinding.FragmentRegisterBinding
import java.io.File
import java.io.FileOutputStream

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var selectedImageUri: Uri? = null

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 101
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeRoleDropdown()
        observeWilayahDropdown()
        setupUploadImage()
        observeRegisterResult()

        binding.btnRegister.setOnClickListener {
            if (!isInputValid()) {
                Toast.makeText(requireContext(), "Lengkapi semua data!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val roleName = binding.etRole.text.toString()
            val wilayahName = binding.etWilayahKerja.text.toString()

            val roleId = viewModel.currentRoles.find { it.name == roleName }?.id
            val wilayahId = viewModel.currentWilayah.find { it.name == wilayahName }?.id

            if (roleId == null || wilayahId == null) {
                Toast.makeText(requireContext(), "Data role atau wilayah tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val savedImagePath = selectedImageUri?.let { saveImageToInternalStorage(it) }
            if (savedImagePath == null) {
                Toast.makeText(requireContext(), "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.onRegisterClicked(
                name = name,
                email = email,
                password = password,
                role = roleId,
                wilayah = wilayahId,
                fotoUri = savedImagePath
            )
        }
    }

    private fun observeRoleDropdown() {
        viewModel.roleList.observe(viewLifecycleOwner) {
            viewModel.currentRoles = it
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                it.map { role -> role.name }
            )
            binding.etRole.setAdapter(adapter)
        }
    }

    private fun observeWilayahDropdown() {
        viewModel.wilayahList.observe(viewLifecycleOwner) {
            viewModel.currentWilayah = it
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                it.map { wilayah -> wilayah.name }
            )
            binding.etWilayahKerja.setAdapter(adapter)
        }
    }

    private fun setupUploadImage() {
        binding.btnUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val fileName = "IMG_${System.currentTimeMillis()}.jpg"
            val file = File(requireContext().filesDir, fileName)
            val outputStream = FileOutputStream(file)

            inputStream?.copyTo(outputStream)

            inputStream?.close()
            outputStream.close()

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.imgPreview.setImageURI(selectedImageUri)
        }
    }

    fun Editable?.isNotNullOrBlank(): Boolean = this?.toString()?.isNotBlank() == true

    private fun isInputValid(): Boolean {
        return binding.etName.text.isNotNullOrBlank() &&
                binding.etEmail.text.isNotNullOrBlank() &&
                binding.etPassword.text.isNotNullOrBlank() &&
                binding.etRole.text?.isNotBlank() == true &&
                binding.etWilayahKerja.text?.isNotBlank() == true &&
                selectedImageUri != null
    }

    private fun observeRegisterResult() {
        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Berhasil daftar!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.loginFragment)
            }
            result.onFailure {
                Toast.makeText(requireContext(), it.message ?: "Gagal daftar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
