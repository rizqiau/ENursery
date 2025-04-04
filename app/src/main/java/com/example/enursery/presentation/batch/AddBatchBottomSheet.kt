package com.example.enursery.presentation.batch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.example.enursery.R
import com.example.enursery.core.domain.model.Batch
import com.example.enursery.core.utils.DateFormatter
import com.example.enursery.core.utils.IdGenerator
import com.example.enursery.databinding.BottomSheetAddBatchBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class AddBatchBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddBatchBinding? = null
    private val binding get() = _binding!!

    var onBatchAdded: ((Batch) -> Unit)? = null

    private var tanggalMulai: LocalDate? = null
    private var tanggalSelesai: LocalDate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddBatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupDatePicker()
        setupNamaBatchInput()
        setupButtonSimpan()
    }

    private fun setupDatePicker() {
        binding.etTanggalMulai.setOnClickListener {
            showDatePicker { selected ->
                tanggalMulai = selected
                binding.etTanggalMulai.setText(DateFormatter.formatTanggalIndonesia(selected))
            }
        }

        binding.etTanggalSelesai.setOnClickListener {
            showDatePicker { selected ->
                tanggalSelesai = selected
                binding.etTanggalSelesai.setText(DateFormatter.formatTanggalIndonesia(selected))
            }
        }
    }

    private fun setupNamaBatchInput() {
        binding.etNamaBatch.doAfterTextChanged {
            binding.etNamaBatch.error = null
        }
    }

    private fun setupButtonSimpan() {
        binding.root.findViewById<Button>(R.id.btnSimpan)?.setOnClickListener {
            handleSimpan()
        }
    }

    private fun handleSimpan() {
        val namaInput = binding.etNamaBatch.text.toString()
        if (namaInput.isBlank()) {
            binding.etNamaBatch.error = "Nama batch tidak boleh kosong"
            return
        }

        if (tanggalMulai == null || tanggalSelesai == null) {
            Toast.makeText(requireContext(), "Tanggal mulai & selesai harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (tanggalSelesai!!.isBefore(tanggalMulai)) {
            Toast.makeText(requireContext(), "Tanggal selesai tidak boleh sebelum tanggal mulai", Toast.LENGTH_SHORT).show()
            return
        }

        val (idBatch, namaBatch) = IdGenerator.generateBatchIdAndName(namaInput)

        val batch = Batch(
            idBatch = idBatch,
            namaBatch = namaBatch,
            tanggalMulai = DateFormatter.toEpochDay(tanggalMulai!!),
            tanggalSelesai = DateFormatter.toEpochDay(tanggalSelesai!!)
        )

        onBatchAdded?.invoke(batch)
        dismiss()
    }

    private fun showDatePicker(onDateSelected: (LocalDate) -> Unit) {
        val today = LocalDate.now()
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Pilih Tanggal")
            .setSelection(today.toEpochDay() * 24 * 60 * 60 * 1000) // convert to millis
            .build()

        datePicker.addOnPositiveButtonClickListener {
            val selectedDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
            onDateSelected(selectedDate)
        }

        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
