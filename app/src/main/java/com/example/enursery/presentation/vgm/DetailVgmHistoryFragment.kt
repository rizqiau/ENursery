package com.example.enursery.presentation.vgm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.enursery.core.ui.VgmHistoryTimelineAdapter
import com.example.enursery.core.ui.ViewModelFactory
import com.example.enursery.databinding.FragmentDetailVgmHistoryBinding

class DetailVgmHistoryFragment : Fragment() {

    private var _binding: FragmentDetailVgmHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: VgmHistoryViewModel
    private lateinit var adapter: VgmHistoryTimelineAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailVgmHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idBibit = arguments?.getString("idBibit")
        if (idBibit.isNullOrBlank()) {
            Toast.makeText(requireContext(), "ID Bibit tidak ditemukan", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        viewModel = ViewModelProvider(
            this, ViewModelFactory.getInstance(requireContext())
        )[VgmHistoryViewModel::class.java]

        setupRecyclerView()

        viewModel.getHistoryByBibit(idBibit).observe(viewLifecycleOwner) { list ->
            adapter = VgmHistoryTimelineAdapter(list)
            binding.rvVgmHistory.adapter = adapter
            binding.tvIdBibit.text = "Riwayat Bibit $idBibit"
            binding.tvEmptyHistory.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupRecyclerView() {
        binding.rvVgmHistory.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
