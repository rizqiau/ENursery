package com.example.enursery.core.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.enursery.core.domain.model.PlotWithVgmCountModel
import com.example.enursery.core.utils.toFormatted
import com.example.enursery.databinding.ItemPlotBinding

class PlotWithVgmAdapter : ListAdapter<PlotWithVgmCountModel, PlotWithVgmAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<PlotWithVgmCountModel>() {
        override fun areItemsTheSame(oldItem: PlotWithVgmCountModel, newItem: PlotWithVgmCountModel) =
            oldItem.idPlot == newItem.idPlot

        override fun areContentsTheSame(oldItem: PlotWithVgmCountModel, newItem: PlotWithVgmCountModel) =
            oldItem == newItem
    }
) {
    var onLongClick: ((PlotWithVgmCountModel) -> Unit)? = null
    inner class ViewHolder(private val binding: ItemPlotBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlotWithVgmCountModel) {
            Log.d("PlotWithVgmAdapter", "Binding plot: ${item.namaPlot} - ${item.jumlahVgm}")
            binding.tvNamaPlot.text = item.namaPlot
            binding.tvLuasArea.text = "${item.luasArea.toFormatted()} ha"
            binding.tvVarietas.text = item.varietas
            binding.tvJumlahBibit.text = "${item.jumlahBibit.toFormatted()} Unit"
            binding.tvJumlahVgm.text = "${item.jumlahVgm.toFormatted()} Sampel"

            binding.root.setOnLongClickListener {
                onLongClick?.invoke(item)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
