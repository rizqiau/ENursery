package com.example.enursery.presentation.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.enursery.R
import com.example.enursery.core.ui.ViewModelFactory
import com.example.enursery.databinding.FragmentProfileBinding
import com.example.enursery.presentation.auth.AuthActivity
import com.github.mikephil.charting.components.XAxis
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
        observeChartData()
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

    private fun observeChartData() {
        viewModel.dailyInputStat?.observe(viewLifecycleOwner) { list ->
            val today = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM")
            // Generate list 7 hari terakhir
            val dates = (0..6).map { today.minusDays((6 - it).toLong()) }
            // Pastikan semua tanggal terisi, meski 0
            val entryList = mutableListOf<BarEntry>()
            val labelList = mutableListOf<String>()

            dates.forEachIndexed { index, date ->
                val jumlah = list?.find { it.tanggal == date }?.jumlahInput ?: 0
                entryList.add(BarEntry(index.toFloat(), jumlah.toFloat()))
                labelList.add(date.format(formatter))
            }

            val dataSet = BarDataSet(entryList, "Jumlah Input").apply {
                valueTextSize = 12f
                color = ContextCompat.getColor(requireContext(), R.color.main)
            }

            dataSet.valueFormatter = object : ValueFormatter() {
                override fun getBarLabel(barEntry: BarEntry?): String {
                    val value = barEntry?.y ?: 0f
                    return if (value == 0f) "" else value.toInt().toString()
                }
            }

            val data = BarData(dataSet)
            binding.barChart.apply {
                this.data = data
                xAxis.valueFormatter = IndexAxisValueFormatter(labelList)
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                xAxis.setDrawGridLines(false)
                axisLeft.isEnabled = false
                axisRight.isEnabled = false
                description.isEnabled = false
                legend.isEnabled = false
                animateY(1000)
                invalidate()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
