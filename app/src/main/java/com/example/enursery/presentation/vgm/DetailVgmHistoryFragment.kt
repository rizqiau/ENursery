package com.example.enursery.presentation.vgm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.enursery.R
import com.example.enursery.core.ui.VgmHistoryAdapter
import com.example.enursery.core.ui.ViewModelFactory
import com.example.enursery.databinding.FragmentDetailVgmHistoryBinding

class DetailVgmHistoryFragment : Fragment() {

    private var _binding: FragmentDetailVgmHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: VgmHistoryViewModel
    private lateinit var adapter: VgmHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailVgmHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idBibit = arguments?.getString("idBibit").orEmpty()
        if (idBibit.isBlank()) {
            Toast.makeText(requireContext(), "ID Bibit tidak ditemukan", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        viewModel = ViewModelProvider(
            this, ViewModelFactory.getInstance(requireContext())
        )[VgmHistoryViewModel::class.java]

        adapter = VgmHistoryAdapter()
        setupRecyclerView()

        observeHistory(idBibit)
    }

    private fun setupRecyclerView() = with(binding.rvVgmHistory) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = this@DetailVgmHistoryFragment.adapter

        val controller = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
        binding.rvVgmHistory.layoutAnimation = controller
    }

    private fun observeHistory(idBibit: String) {
        viewModel.getHistoryByBibit(idBibit).observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.rvVgmHistory.scheduleLayoutAnimation()
            binding.tvIdBibit.text = "Riwayat Bibit $idBibit"
            binding.tvEmptyHistory.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE

            // Timeline decoration after data is set
            binding.rvVgmHistory.apply {
                // clear previous decoration (avoid stacking)
                while (itemDecorationCount > 0) {
                    removeItemDecorationAt(0)
                }
                addItemDecoration(TimelineItemDecoration(requireContext(), list))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
