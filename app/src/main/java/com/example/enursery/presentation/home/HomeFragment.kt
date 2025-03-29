package com.example.enursery.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.enursery.R
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.ui.PlotWithVgmAdapter
import com.example.enursery.core.ui.ViewModelFactory

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var plotAdapter: PlotWithVgmAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        plotAdapter = PlotWithVgmAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_plot)
        recyclerView.adapter = plotAdapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        observeData()
        Log.d("HomeFragment", "onViewCreated jalan")
    }

    private fun observeData() {
        viewModel.plots.observe(viewLifecycleOwner) {
            // Gak perlu ngapa-ngapain, ini cuma buat jalanin NetworkBoundResource
        }
        viewModel.plotWithVgm.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    // tampilkan progress
                }
                is Resource.Success -> {
                    result.data?.let { plotAdapter.submitList(it) }
                    Log.d("HomeFragment", "Jumlah data masuk ke adapter: ${result.data?.size}")
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Gagal: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
