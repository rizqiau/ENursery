package com.example.enursery.presentation.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.enursery.R
import com.example.enursery.core.ui.PlotProgressAdapter
import com.example.enursery.core.ui.ViewModelFactory
import com.example.enursery.databinding.FragmentProfileBinding
import com.example.enursery.presentation.auth.AuthActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel
    private lateinit var progressAdapter: PlotProgressAdapter

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

        viewModel.plotProgressList?.observe(viewLifecycleOwner) { list ->
            Log.d("ProfileFragment", "Plot Progress List Size: ${list.size}")
            list.forEach {
                Log.d("ProfileFragment", "Plot: ${it.namaPlot}, Target: ${it.jumlahTarget}, Input: ${it.jumlahInput}")
            }
        }


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
        observeChartData()
        observePlotProgress()
    }

    private fun observePlotProgress() {
        viewModel.plotProgressList?.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                progressAdapter = PlotProgressAdapter(list)
                binding.rvPlotProgress.apply {
                    layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    adapter = progressAdapter
                }
            } else {
                binding.rvPlotProgress.visibility = View.GONE
                binding.tvEmptyPlot.visibility =View.VISIBLE
            }
        }
    }

    private fun observeUser() {
        viewModel.currentUser?.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.tvNama.text = it.namaUser
                binding.tvId.text = it.idUser
                binding.tvRole.text = it.role
                binding.chipWilayah.text = it.wilayahKerja

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

    private fun setupBarChart(entries: List<BarEntry>, labels: List<String>) = with(binding.barChart) {
        setNoDataText("Belum ada data input.")
        description.isEnabled = false
        legend.isEnabled = false
        setDrawBorders(false)
        setDrawGridBackground(false) // ← OFF, biar flat
        setExtraOffsets(12f, 16f, 12f, 16f)
        animateY(800, Easing.EaseInOutQuad)

        marker = CustomMarkerView(context, R.layout.custom_marker_view, labels)

        xAxis.configureBottomAxis(context, labels)
        axisLeft.configureLeftAxis(context, labels)
        axisRight.isEnabled = false

        val barDataSet = BarDataSet(entries, "Jumlah Input").apply {
            color = ContextCompat.getColor(context, R.color.main)
            valueTextColor = ContextCompat.getColor(context, R.color.textColorPrimary) // cocok buat dark
            valueTextSize = 12f
            highLightAlpha = 0
            valueFormatter = object : ValueFormatter() {
                override fun getBarLabel(barEntry: BarEntry?): String {
                    return barEntry?.y?.toInt()?.takeIf { it > 0 }?.toString() ?: ""
                }
            }
        }

        data = BarData(barDataSet).apply {
            barWidth = 0.4f // ← Lebih ramping
        }

        setFitBars(true) // ← Auto-scale agar pas
        invalidate()
    }

    private fun observeChartData() {
        viewModel.dailyInputStat?.observe(viewLifecycleOwner) { list ->
            val today = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM")
            val last7Days = (0..6).map { today.minusDays((6 - it).toLong()) }

            val entries = mutableListOf<BarEntry>()
            val labels = mutableListOf<String>()
            var total = 0

            last7Days.forEachIndexed { index, date ->
                val jumlah = list.orEmpty().firstOrNull { it.tanggal == date }?.jumlahInput ?: 0
                total += jumlah
                entries.add(BarEntry(index.toFloat(), jumlah.toFloat()))
                labels.add(date.format(formatter))
            }

            if (total == 0) showEmptyChartOverlay()
            else {
                binding.tvEmptyChartOverlay.visibility = View.GONE
                setupBarChart(entries, labels)
            }
        }
    }

    private fun showEmptyChartOverlay() {
        binding.barChart.clear()
        binding.barChart.invalidate()
        binding.barChart.visibility = View.GONE
        binding.tvEmptyChartOverlay.apply {
            visibility = View.VISIBLE
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
        }
    }

    private fun XAxis.configureBottomAxis(context: Context, labels: List<String>) {
        position = XAxis.XAxisPosition.BOTTOM
        setDrawAxisLine(false)
        setDrawGridLines(false)
        granularity = 1f
        valueFormatter = IndexAxisValueFormatter(labels)
        textColor = ContextCompat.getColor(context, R.color.textColorPrimary) // <- Sesuaikan
        textSize = 11f
        labelRotationAngle = 0f // ← Lebih clean
    }

    private fun YAxis.configureLeftAxis(context: Context, labels: List<String>) {
        setDrawAxisLine(false)
        setDrawGridLines(true)
        gridColor = ContextCompat.getColor(context, R.color.textColorPrimary)
        axisMinimum = 0f
        textColor = ContextCompat.getColor(context, R.color.textColorPrimary)
        textSize = 11f
        valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value > 0f) value.toInt().toString() else ""
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
