package com.example.enursery.presentation.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.enursery.R
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.di.Injection
import com.example.enursery.core.domain.model.PlotWithVgmCountModel
import com.example.enursery.core.domain.usecase.user.SessionUseCase
import com.example.enursery.core.ui.PlotWithVgmAdapter
import com.example.enursery.core.ui.ViewModelFactory
import com.example.enursery.core.utils.CsvExporter
import com.example.enursery.core.utils.canAddPlot
import com.example.enursery.databinding.BottomSheetPlotActionBinding
import com.example.enursery.databinding.FragmentHomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var plotAdapter: PlotWithVgmAdapter
    private lateinit var sessionUseCase: SessionUseCase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionUseCase = Injection.provideSessionUseCase(requireContext())

        plotAdapter = PlotWithVgmAdapter().apply {
            onLongClick = { plot ->
                showPlotOptionsBottomSheet(plot)
            }
        }

        binding.rvPlot.apply {
            adapter = plotAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            layoutAnimation = AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.layout_animation_fall_down
            )
        }

        observeData()

        val role = sessionUseCase.getUserRole()
        if (role != null && role.canAddPlot()) {
            binding.btnAddPlot.visibility = View.VISIBLE
            binding.btnAddPlot.setOnClickListener {
                val bundle = bundleOf("mode" to "ADD")
                findNavController().navigate(R.id.action_homeFragment_to_addEditPlotFragment, bundle)
            }
        } else {
            binding.btnAddPlot.visibility = View.GONE
        }
    }

    private fun observeData() {
        viewModel.getAllPlots().observe(viewLifecycleOwner) {
            // Optional logging
        }

        viewModel.getPlotWithVgm().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    // TODO: Tambahkan shimmer/loading
                }
                is Resource.Success -> {
                    val data = result.data.orEmpty()
                    plotAdapter.submitList(data) {
                        binding.rvPlot.scheduleLayoutAnimation()
                    }

                    // ✅ Tampilkan atau sembunyikan tvEmptyChartOverlay
                    binding.tvEmptyChartOverlay.visibility =
                        if (data.isEmpty()) View.VISIBLE else View.GONE
                }
                is Resource.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Gagal: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showPlotOptionsBottomSheet(plot: PlotWithVgmCountModel) {
        val bindingSheet = BottomSheetPlotActionBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bindingSheet.root)

        bindingSheet.tvEditPlot.setOnClickListener {
            bottomSheetDialog.dismiss()
            val bundle = bundleOf("mode" to "EDIT", "plotId" to plot.idPlot)
            findNavController().navigate(R.id.action_homeFragment_to_addEditPlotFragment, bundle)
        }

        bindingSheet.tvDeletePlot.setOnClickListener {
            bottomSheetDialog.dismiss()
            showDeleteConfirmation(plot.idPlot)
        }

        bindingSheet.tvLihatBaris.setOnClickListener {
            val idPlot = plot.idPlot
            viewModel.getPlotWithBaris(idPlot).observe(viewLifecycleOwner) { data ->
                data?.let {
                    val uri = CsvExporter.exportPlotWithBarisToCsv(
                        context = requireContext(),
                        plot = it // cukup it aja, karena it adalah PlotWithBarisModel
                    )
                    Toast.makeText(requireContext(), "Berhasil diekspor ke Downloads", Toast.LENGTH_SHORT).show()
                } ?: Toast.makeText(requireContext(), "Gagal ekspor", Toast.LENGTH_SHORT).show()
            }
            bottomSheetDialog.dismiss()
        }

        // ⬇️ Tambahkan ini supaya dialog muncul
        bottomSheetDialog.show()
    }

    private fun showDeleteConfirmation(idPlot: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Plot")
            .setMessage("Yakin ingin menghapus plot ini?")
            .setPositiveButton("Ya") { _, _ ->
                viewModel.deletePlot(idPlot)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
