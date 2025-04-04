package com.example.enursery.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.enursery.R

class BarisIdBibitAdapter(
    private val data: List<Pair<String, List<String>>>
) : RecyclerView.Adapter<BarisIdBibitAdapter.BarisViewHolder>() {

    inner class BarisViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaBaris = view.findViewById<TextView>(R.id.tvNamaBaris)
        val tvListIdBibit = view.findViewById<TextView>(R.id.tvListIdBibit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarisViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_id_bibit_per_baris, parent, false)
        return BarisViewHolder(view)
    }

    override fun onBindViewHolder(holder: BarisViewHolder, position: Int) {
        val (baris, idList) = data[position]
        holder.tvNamaBaris.text = "Baris $baris"
        holder.tvListIdBibit.text = idList.joinToString(", ")
    }

    override fun getItemCount() = data.size
}
