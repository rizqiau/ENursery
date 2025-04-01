package com.example.enursery.core.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.enursery.R
import com.example.enursery.core.domain.model.Vgm
import com.example.enursery.databinding.ItemVgmBinding

class VgmAdapter : RecyclerView.Adapter<VgmAdapter.ViewHolder>() {
    private val list = ArrayList<Vgm>()

    fun submitList(data: List<Vgm>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemVgmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(vgm: Vgm) {
            binding.tvBibitId.text = vgm.idBibit
            binding.tvTinggiTanaman.text = vgm.latestTinggiTanaman.toString()
            binding.tvDiameterBatang.text = vgm.latestDiameterBatang.toString()
            binding.tvJumlahDaun.text = vgm.latestJumlahDaun.toString()
            Glide.with(binding.ivVgmImage.context)
                .load(Uri.parse(vgm.latestFoto))
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.ivVgmImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVgmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}