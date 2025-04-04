package com.example.enursery.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.enursery.core.ui.BarisIdBibitAdapter
import com.example.enursery.databinding.BottomSheetBarisIdBibitBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BarisIdBibitBottomSheet(
    private val barisWithIdBibit: List<Pair<String, List<String>>>
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetBarisIdBibitBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BarisIdBibitAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomSheetBarisIdBibitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = BarisIdBibitAdapter(barisWithIdBibit)
        binding.rvBarisIdBibit.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@BarisIdBibitBottomSheet.adapter // ← pakai yang udah diinit
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
