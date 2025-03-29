package com.example.enursery.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.enursery.core.domain.model.Plot
import com.example.enursery.databinding.ItemPlotBinding

class PlotAdapter : RecyclerView.Adapter<PlotAdapter.ViewHolder>() {

    private val list = ArrayList<Plot>()

    fun submitList(data: List<Plot>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemPlotBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(plot: Plot) {
            binding.tvNamaPlot.text = plot.namaPlot
            // dst...
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}
