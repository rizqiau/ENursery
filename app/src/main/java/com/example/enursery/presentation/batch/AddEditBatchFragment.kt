package com.example.enursery.presentation.batch

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.enursery.core.domain.model.Batch
import com.example.enursery.core.ui.ViewModelFactory
import com.example.enursery.databinding.FragmentAddEditBatchBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class AddEditBatchFragment : Fragment() {

    private var _binding: FragmentAddEditBatchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BatchViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var mode: String = "ADD"
    private var currentBatchId: String? = null
    private var currentBatch: Batch? = null

    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale("id", "ID"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditBatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mode = arguments?.getString("mode") ?: "ADD"
        currentBatchId = arguments?.getString("batchId")

        if (mode == "EDIT" && currentBatchId != null) {
            observeBatch()
            binding.btnSimpanBatch.text = "Update"
        } else {
            binding.btnSimpanBatch.text = "Simpan"
        }

        binding.btnSimpanBatch.setOnClickListener {
            if (mode == "EDIT") updateBatch() else insertBatch()
        }

        setupDatePickers()
    }

    private fun setupDatePickers() {
        binding.etTanggalMulai.setOnClickListener {
            showDatePicker { binding.etTanggalMulai.setText(it) }
        }
        binding.etTanggalSelesai.setOnClickListener {
            showDatePicker { binding.etTanggalSelesai.setText(it) }
        }
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val date = LocalDate.of(year, month + 1, day).format(formatter)
                onDateSelected(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun observeBatch() {
        viewModel.batchList.observe(viewLifecycleOwner) { list ->
            val found = list.find { it.idBatch == currentBatchId }
            found?.let {
                currentBatch = it
                bindToForm(it)
            }
        }
    }

    private fun bindToForm(batch: Batch) = binding.apply {
        etNamaBatch.setText(batch.namaBatch)
        etTanggalMulai.setText(batch.tanggalMulai.format(formatter))
        etTanggalSelesai.setText(batch.tanggalSelesai.format(formatter))
    }

    private fun insertBatch() {
        val batch = buildBatch() ?: return
        viewModel.insertBatch(batch)
        Toast.makeText(requireContext(), "Batch berhasil ditambahkan", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    private fun updateBatch() {
        val batch = buildBatch(currentBatch?.idBatch) ?: return
        viewModel.updateBatch(batch)
        Toast.makeText(requireContext(), "Batch berhasil diperbarui", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    private fun buildBatch(idOverride: String? = null): Batch? {
        val nama = binding.etNamaBatch.text.toString().trim()
        val mulaiStr = binding.etTanggalMulai.text.toString()
        val selesaiStr = binding.etTanggalSelesai.text.toString()

        if (nama.isBlank() || mulaiStr.isBlank() || selesaiStr.isBlank()) {
            Toast.makeText(requireContext(), "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
            return null
        }

        val mulai = LocalDate.parse(mulaiStr, formatter)
        val selesai = LocalDate.parse(selesaiStr, formatter)
        val id = idOverride ?: "BATCH_${nama.uppercase().replace(" ", "_")}"

        return Batch(
            idBatch = id,
            namaBatch = nama,
            tanggalMulai = mulai,
            tanggalSelesai = selesai
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
