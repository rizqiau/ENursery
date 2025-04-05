package com.example.enursery.core.utils

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.example.enursery.R
import java.text.DateFormatSymbols
import java.time.LocalDate

class MonthYearPickerDialog : DialogFragment() {

    private lateinit var spinnerBulan: Spinner
    private lateinit var spinnerTahun: Spinner

    var onDateSelected: ((month: Int, year: Int) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_month_year_picker, null)

        spinnerBulan = view.findViewById(R.id.spinnerBulan)
        spinnerTahun = view.findViewById(R.id.spinnerTahun)

        setupSpinners()

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setTitle("Pilih Bulan & Tahun")
            .setPositiveButton("OK") { _, _ ->
                val month = spinnerBulan.selectedItemPosition + 1
                val year = spinnerTahun.selectedItem.toString().toInt()
                onDateSelected?.invoke(month, year)
            }
            .setNegativeButton("Batal", null)
            .create()
    }

    private fun setupSpinners() {
        val bulanList = DateFormatSymbols().months.take(12)
        val today = LocalDate.now()
        val tahunSekarang = today.year
        val tahunList = (2000..tahunSekarang).toList()

        spinnerBulan.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bulanList).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerTahun.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tahunList).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // 👉 Set default selection ke bulan & tahun saat ini
        spinnerBulan.setSelection(today.monthValue - 1)
        spinnerTahun.setSelection(tahunList.indexOf(today.year))
    }
}
