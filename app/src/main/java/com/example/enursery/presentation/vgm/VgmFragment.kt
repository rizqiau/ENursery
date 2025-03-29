package com.example.enursery.presentation.vgm

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.enursery.R
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.ui.VgmAdapter
import com.example.enursery.core.ui.ViewModelFactory

class VgmFragment : Fragment(R.layout.fragment_vgm) {
    private val viewModel: VgmViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: VgmAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = VgmAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_vgm)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        observeVgm()
    }

    private fun observeVgm() {
        viewModel.vgm.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    // Optional: tampilkan progress bar
                }
                is Resource.Success -> {
                    adapter.submitList(result.data.orEmpty())
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), result.message ?: "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}