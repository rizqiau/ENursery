package com.example.enursery.core.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.enursery.R
import com.example.enursery.core.domain.model.Baris
import com.google.android.material.textfield.TextInputEditText

class BarisAdapter(
    private val barisList: MutableList<Baris>
) : RecyclerView.Adapter<BarisAdapter.BarisViewHolder>() {

    inner class BarisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaBaris: TextView = itemView.findViewById(R.id.tvNamaBaris)
        val etJumlahVgm: TextInputEditText = itemView.findViewById(R.id.etJumlahVgm)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarisViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_baris, parent, false)
        return BarisViewHolder(view)
    }

    override fun onBindViewHolder(holder: BarisViewHolder, position: Int) {
        val baris = barisList[position]

        holder.tvNamaBaris.text = baris.namaBaris

        // Set text jika sebelumnya sudah ada
        holder.etJumlahVgm.setText(
            if (baris.jumlahTargetVgm > 0) baris.jumlahTargetVgm.toString() else ""
        )

        // Remove listener dulu untuk cegah update berulang saat scroll
        holder.etJumlahVgm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val adapterPosition = holder.adapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val jumlah = s?.toString()?.toIntOrNull() ?: 0
                    barisList[adapterPosition] = barisList[adapterPosition].copy(jumlahTargetVgm = jumlah)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun getItemCount(): Int = barisList.size

    fun getUpdatedBarisList(): List<Baris> = barisList.toList()
}
