package com.example.enursery.presentation.vgm

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.enursery.R
import com.example.enursery.core.data.source.Resource
import com.example.enursery.core.domain.model.DateFilterType
import com.example.enursery.core.domain.model.SortOption
import com.example.enursery.core.ui.VgmAdapter
import com.example.enursery.core.ui.ViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class VgmFragment : Fragment(R.layout.fragment_vgm) {

    private val viewModel: VgmViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var vgmAdapter: VgmAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var spinnerSort: Spinner
    private lateinit var spinnerBatch: Spinner
    private lateinit var spinnerPlot: Spinner
    private lateinit var spinnerDate: TextView
    private lateinit var tvEmptyOverlay: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewReferences(view)
        setupRecyclerView()
        setupSearchField()
        setupBatchSpinner()
        setupSortSpinner()
        setupPlotSpinner()
        setupDateFilter()
        observeFilteredData()
    }

    private fun setupViewReferences(view: View) {
        recyclerView = view.findViewById(R.id.rv_vgm)
        etSearch = view.findViewById(R.id.etSearch)
        spinnerSort = view.findViewById(R.id.spinnerVgm)
        spinnerBatch = view.findViewById(R.id.spinnerBatch)
        spinnerPlot = view.findViewById(R.id.spinnerPlot)
        spinnerDate = view.findViewById(R.id.spinnerDate)
        tvEmptyOverlay = view.findViewById(R.id.tvEmptyVgmOverlay)
    }

    private fun setupRecyclerView() {
        vgmAdapter = VgmAdapter { vgmItem ->
            val bundle = bundleOf("idBibit" to vgmItem.idBibit)
            findNavController().navigate(R.id.action_vgmFragment_to_detailVgmHistoryFragment, bundle)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = vgmAdapter
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSearchField() {
        updateSearchIcon()

        etSearch.addTextChangedListener {
            updateSearchIcon()
            viewModel.setSearchQuery(it.toString())
        }

        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                etSearch.clearFocus()
                hideKeyboard()
                true
            } else false
        }

        etSearch.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val closeIcon = etSearch.compoundDrawables[2]
                val drawableStart = etSearch.width - etSearch.paddingEnd - (closeIcon?.intrinsicWidth ?: 0)
                if (event.x >= drawableStart) {
                    etSearch.setText("")
                    etSearch.clearFocus()
                    hideKeyboard()
                    updateSearchIcon()
                    return@setOnTouchListener true
                }
            }
            false
        }

        etSearch.setOnFocusChangeListener { _, _ ->
            updateSearchIcon()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (etSearch.hasFocus()) {
                etSearch.clearFocus()
                hideKeyboard()
            } else {
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }
    }

    private fun updateSearchIcon() {
        val searchIcon = if (etSearch.text.isNullOrEmpty() && !etSearch.hasFocus()) {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_search)
        } else null

        val closeIcon = if (!etSearch.text.isNullOrEmpty() || etSearch.hasFocus()) {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_close)
        } else null

        etSearch.setCompoundDrawablesWithIntrinsicBounds(searchIcon, null, closeIcon, null)
        etSearch.isCursorVisible = etSearch.hasFocus() || !etSearch.text.isNullOrEmpty()
    }

    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
    }

    private fun setupSortSpinner() {
        val sortOptions = SortOption.values().toList()

        val adapterSort = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            sortOptions.map {
                when (it) {
                    SortOption.TERBARU -> "Terbaru"
                    SortOption.TERLAMA -> "Terlama"
                    SortOption.ID_AZ -> "A-Z"
                    SortOption.ID_ZA -> "Z-A"
                }
            }
        )
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSort.adapter = adapterSort

        spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                viewModel.setSortOption(sortOptions[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupBatchSpinner() {
        viewModel.batchList.observe(viewLifecycleOwner) { batchList ->
            val batchNames = mutableListOf("Semua Batch")
            batchNames += batchList.map { it.namaBatch }

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                batchNames
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            spinnerBatch.adapter = adapter

            spinnerBatch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selected = if (position == 0) null else batchList[position - 1].idBatch
                    viewModel.setSelectedBatch(selected)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }

    private fun setupPlotSpinner() {
        viewModel.plotList.observe(viewLifecycleOwner) { resource ->
            if (resource is Resource.Success) {
                val plotList = resource.data ?: return@observe
                viewModel.currentPlot = plotList

                val namaPlotList = mutableListOf("Semua Plot") + plotList.map { it.namaPlot }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    namaPlotList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerPlot.adapter = adapter

                spinnerPlot.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        val selected = if (position == 0) null else plotList[position - 1].idPlot
                        viewModel.setSelectedPlot(selected)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }
            }
        }
    }

    private fun setupDateFilter() {
        spinnerDate.setOnClickListener {
            DateFilterBottomSheetFragment().show(childFragmentManager, "DateFilterBottomSheet")
        }

        viewModel.selectedDateFilter.observe(viewLifecycleOwner) { filter ->
            updateDateFilterLabel(filter, viewModel.selectedDateRange.value)
        }

        viewModel.selectedDateRange.observe(viewLifecycleOwner) { range ->
            updateDateFilterLabel(viewModel.selectedDateFilter.value, range)
        }

        childFragmentManager.setFragmentResultListener("date_filter_result", viewLifecycleOwner) { _, bundle ->
            val filterType = bundle.getSerializable("filterType") as? DateFilterType
            val date = bundle.getString("date")?.let { LocalDate.parse(it) }
            val start = bundle.getString("startDate")?.let { LocalDate.parse(it) }
            val end = bundle.getString("endDate")?.let { LocalDate.parse(it) }
            val status = bundle.getString("status")

            // ✅ Tambahkan ini agar status ke-update
            viewModel.setSelectedStatus(status)
            Log.d("VgmFragment", "Status dari bottom sheet: $status")

            if (filterType == null) {
                viewModel.resetDateFilter()
                updateDateFilterLabel(null, null)
            } else {
                when (filterType) {
                    DateFilterType.PILIH_RENTANG_TANGGAL -> {
                        if (start != null && end != null) {
                            viewModel.setDateRangeFilter(start, end)
                        }
                    }

                    else -> {
                        viewModel.setDateFilter(filterType, date)
                        updateDateFilterLabel(Pair(filterType, date))
                    }
                }
            }
        }
    }

    private fun updateDateFilterLabel(
        filter: Pair<DateFilterType, LocalDate?>?,
        range: Pair<LocalDate, LocalDate>? = null
    ) {
        val label = when (filter?.first) {
            DateFilterType.HARI_INI -> "Hari Ini"
            DateFilterType.TUJUH_HARI_TERAKHIR -> "7 Hari Terakhir"
            DateFilterType.PILIH_BULAN -> filter.second?.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
            DateFilterType.PILIH_RENTANG_TANGGAL -> {
                range?.let {
                    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
                    "${it.first.format(formatter)} - ${it.second.format(formatter)}"
                } ?: "Rentang Tanggal"
            }
            else -> "Semua Tanggal"
        }
        spinnerDate.text = label ?: "Semua Tanggal"
    }

    private fun observeFilteredData() {
        viewModel.filteredSortedVgm.observe(viewLifecycleOwner) { list ->
            Log.d("VgmFragment", "Filtered VGM size: ${list.size}")
            list.forEach {
                Log.d("VgmFragment", "${it.idBibit} → status: ${it.status}")
            }
            vgmAdapter.submitList(list)

            tvEmptyOverlay.visibility = if (list.isNullOrEmpty()) View.VISIBLE else View.GONE
        }
    }
}

