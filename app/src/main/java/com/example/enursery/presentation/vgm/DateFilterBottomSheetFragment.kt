package com.example.enursery.presentation.vgm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.setFragmentResult
import com.example.enursery.R
import com.example.enursery.core.domain.model.DateFilterType
import com.example.enursery.core.utils.MonthYearPickerDialog
import com.example.enursery.databinding.FragmentDateFilterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateFilterBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDateFilterBottomSheetBinding

    private var selectedFilterType: DateFilterType? = null
    private var selectedSingleDate: LocalDate? = null
    private var selectedStartDate: LocalDate = LocalDate.now()
    private var selectedEndDate: LocalDate = LocalDate.now()
    private var selectedStatus: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDateFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val statusOptions = listOf("Semua Status") + VgmStatus.entries.map { it.label }
        val statusAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, statusOptions)
        binding.etStatus.setAdapter(statusAdapter)

        binding.etStatus.setOnItemClickListener { _, _, position, _ ->
            selectedStatus = if (position == 0) null else statusOptions[position]
            Log.d("BottomSheetStatus", "Status selected: $selectedStatus")
        }

        binding.optionHariIni.setOnClickListener {
            selectedFilterType = DateFilterType.HARI_INI
            selectedSingleDate = null
            binding.containerRangeTanggal.visibility = View.GONE
            binding.radioGroup.check(binding.optionHariIni.id)
        }

        binding.option7Hari.setOnClickListener {
            selectedFilterType = DateFilterType.TUJUH_HARI_TERAKHIR
            selectedSingleDate = null
            binding.containerRangeTanggal.visibility = View.GONE
            binding.radioGroup.check(binding.option7Hari.id)
        }

        binding.optionPilihBulan.setOnClickListener {
            binding.radioGroup.check(binding.optionPilihBulan.id)
            showCustomMonthYearPicker()
        }

        binding.optionPilihTanggal.setOnClickListener {
            binding.radioGroup.check(binding.optionPilihTanggal.id)
            selectedFilterType = DateFilterType.PILIH_RENTANG_TANGGAL
            selectedStartDate = LocalDate.now()
            selectedEndDate = LocalDate.now()
            updateRangeUI()
            binding.containerRangeTanggal.visibility = View.VISIBLE
        }

        binding.tvTanggalMulai.setOnClickListener {
            showDateRangePicker()
        }

        binding.tvTanggalAkhir.setOnClickListener {
            showDateRangePicker()
        }

        binding.btnApply.setOnClickListener {
            when (selectedFilterType) {
                DateFilterType.PILIH_RENTANG_TANGGAL -> {
                    sendRangeResult(selectedStartDate, selectedEndDate)
                }
                DateFilterType.PILIH_BULAN,
                DateFilterType.HARI_INI,
                DateFilterType.TUJUH_HARI_TERAKHIR -> {
                    sendResult(selectedFilterType!!, selectedSingleDate)
                }
                null -> {
                    // ⬅️ User hanya pilih status, tanpa pilih tanggal
                    sendResult(null, null)
                }
            }
        }

        binding.btnReset.setOnClickListener {
            selectedFilterType = null
            selectedSingleDate = null
            selectedStartDate = LocalDate.now()
            selectedEndDate = LocalDate.now()
            selectedStatus = null // ⬅

            binding.radioGroup.clearCheck()
            binding.containerRangeTanggal.visibility = View.GONE
            binding.tvTanggalMulai.text = ""
            binding.tvTanggalAkhir.text = ""
            binding.etStatus.setText("") // ⬅ clear isian

            sendResult(null, null)
        }
    }

    private fun updateRangeUI() {
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        binding.tvTanggalMulai.text = selectedStartDate.format(formatter)
        binding.tvTanggalAkhir.text = selectedEndDate.format(formatter)
    }

    private fun showDateRangePicker() {
        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Pilih Rentang Tanggal")
            .setTheme(R.style.ThemeMaterialCalendar)
            .build()

        picker.show(parentFragmentManager, "DATE_RANGE_PICKER")

        picker.addOnPositiveButtonClickListener { dateRange ->
            val startMillis = dateRange.first
            val endMillis = dateRange.second
            if (startMillis != null && endMillis != null) {
                selectedStartDate = Instant.ofEpochMilli(startMillis).atZone(ZoneId.systemDefault()).toLocalDate()
                selectedEndDate = Instant.ofEpochMilli(endMillis).atZone(ZoneId.systemDefault()).toLocalDate()
                updateRangeUI()
            }
        }
    }

    private fun showCustomMonthYearPicker() {
        val dialog = MonthYearPickerDialog()
        dialog.onDateSelected = { month, year ->
            val selectedDate = LocalDate.of(year, month, 1)
            selectedFilterType = DateFilterType.PILIH_BULAN
            selectedSingleDate = selectedDate
            binding.containerRangeTanggal.visibility = View.GONE
        }
        dialog.show(childFragmentManager, "MonthYearPicker")
    }

    private fun sendResult(filterType: DateFilterType?, value: LocalDate? = null) {
        val result = Bundle().apply {
            putSerializable("filterType", filterType)
            value?.let { putString("date", it.toString()) }
            selectedStatus?.let { putString("status", it) } // ← ⬅ Tambahkan status
            Log.d("BottomSheetStatus", "Sending status: $selectedStatus")
        }
        setFragmentResult("date_filter_result", result)
        dismiss()
    }

    private fun sendRangeResult(start: LocalDate, end: LocalDate) {
        val result = Bundle().apply {
            putSerializable("filterType", DateFilterType.PILIH_RENTANG_TANGGAL)
            putString("startDate", start.toString())
            putString("endDate", end.toString())
            selectedStatus?.let { putString("status", it) } // ← ⬅ Tambahkan status
        }
        setFragmentResult("date_filter_result", result)
        dismiss()
    }

    enum class VgmStatus(val label: String) {
        AKTIF("AKTIF"),
        MATI("MATI");

        override fun toString(): String = label
    }
}
