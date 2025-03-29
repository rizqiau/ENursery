package com.example.enursery.presentation.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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

        observeRoleSpinner()
        observeWilayahSpinner()
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
            val roleIndex = binding.spinnerRole.selectedItemPosition
            val roleId = viewModel.currentRoles[roleIndex].id
            val wilayahIndex = binding.spinnerWilayah.selectedItemPosition
            val wilayahId = viewModel.currentWilayah[wilayahIndex].id

            viewModel.onRegisterClicked(
                name = name,
                email = email,
                password = password,
                role = roleId,
                wilayah = wilayahId,
                fotoUri = selectedImageUri
            )
        }
    }

    private fun observeRoleSpinner() {
        viewModel.roleList.observe(viewLifecycleOwner) {
            viewModel.currentRoles = it
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, it.map { r -> r.name })
            binding.spinnerRole.adapter = adapter
        }
    }

    private fun observeWilayahSpinner() {
        viewModel.wilayahList.observe(viewLifecycleOwner) {
            viewModel.currentWilayah = it
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, it.map { w -> w.name })
            binding.spinnerWilayah.adapter = adapter
        }
    }

    private fun setupUploadImage() {
        binding.btnUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.imgPreview.setImageURI(selectedImageUri)
        }
    }

    private fun isInputValid(): Boolean {
        return binding.etName.text.isNotBlank() &&
                binding.etEmail.text.isNotBlank() &&
                binding.etPassword.text.isNotBlank() &&
                selectedImageUri != null
    }

    private fun observeRegisterResult() {
        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Berhasil daftar!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.navigation_login)
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
