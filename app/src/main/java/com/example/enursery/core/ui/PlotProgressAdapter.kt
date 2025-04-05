package com.example.enursery.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.enursery.core.domain.model.PlotProgressModel
import com.example.enursery.databinding.ItemPlotProgressBinding

class PlotProgressAdapter(
    private val list: List<PlotProgressModel>
) : RecyclerView.Adapter<PlotProgressAdapter.PlotProgressViewHolder>() {

    inner class PlotProgressViewHolder(private val binding: ItemPlotProgressBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PlotProgressModel) {
            binding.tvNamaPlot.text = item.namaPlot
            binding.tvProgress.text = "${item.jumlahInput}/${item.jumlahTarget} bibit"

            val progress = if (item.jumlahTarget == 0) 0
            else (item.jumlahInput * 100) / item.jumlahTarget

            binding.progressBar.progress = progress
            binding.tvPersentase.text = "$progress%"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlotProgressViewHolder {
        val binding = ItemPlotProgressBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlotProgressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlotProgressViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}
