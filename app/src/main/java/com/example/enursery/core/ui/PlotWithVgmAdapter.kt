package com.example.enursery.core.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.enursery.R
import com.example.enursery.core.domain.model.PlotWithVgmCountModel

class PlotWithVgmAdapter : ListAdapter<PlotWithVgmCountModel, PlotWithVgmAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<PlotWithVgmCountModel>() {
        override fun areItemsTheSame(oldItem: PlotWithVgmCountModel, newItem: PlotWithVgmCountModel) = oldItem.idPlot == newItem.idPlot
        override fun areContentsTheSame(oldItem: PlotWithVgmCountModel, newItem: PlotWithVgmCountModel) = oldItem == newItem
    }
) {
    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: PlotWithVgmCountModel) {
            Log.d("PlotWithVgmAdapter", "Binding plot: ${item.namaPlot} - ${item.jumlahVgm}")
            view.findViewById<TextView>(R.id.tvNamaPlot).text = item.namaPlot
            view.findViewById<TextView>(R.id.tvLuasArea).text = item.luasArea.toString()
            view.findViewById<TextView>(R.id.tvJumlahBibit).text = item.jumlahBibit.toString()
            view.findViewById<TextView>(R.id.tvJumlahVgm).text = "Jumlah VGM: ${item.jumlahVgm}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plot, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
