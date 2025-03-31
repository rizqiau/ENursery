package com.example.enursery.presentation.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.enursery.core.ui.ViewModelFactory
import com.example.enursery.databinding.FragmentProfileBinding
import com.example.enursery.presentation.auth.AuthActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))
            .get(ProfileViewModel::class.java)

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Keluar Aplikasi")
                .setMessage("Yakin ingin logout?")
                .setPositiveButton("Ya") { _, _ ->
                    viewModel.logout()
                    startActivity(Intent(requireContext(), AuthActivity::class.java))
                    requireActivity().finish()
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        observeUser()
    }

    private fun observeUser() {
        viewModel.currentUser?.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.tvNama.text = it.nama
                binding.tvId.text = it.email
                binding.tvRole.text = it.role
                binding.tvWilayah.text = it.wilayahKerja

                if (it.foto.startsWith("content://") || it.foto.startsWith("file://")) {
                    binding.ivFoto.setImageURI(Uri.parse(it.foto))
                } else {
                    // fallback jika bukan URI
                    Glide.with(this)
                        .load(it.foto)
                        .into(binding.ivFoto)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
