package com.example.enursery.core.ui

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.enursery.R
import com.example.enursery.core.domain.model.VgmWithUserModel
import com.example.enursery.databinding.ItemVgmBinding

class VgmAdapter : ListAdapter<VgmWithUserModel, VgmAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemVgmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vgmWithUserModel: VgmWithUserModel) {
            Log.d("VgmAdapter", "Bind ${vgmWithUserModel.idBibit}")
            binding.tvBibitId.text = vgmWithUserModel.idBibit
            binding.tvTinggiTanaman.text = vgmWithUserModel.latestTinggiTanaman.toString()
            binding.tvDiameterBatang.text = vgmWithUserModel.latestDiameterBatang.toString()
            binding.tvJumlahDaun.text = vgmWithUserModel.latestJumlahDaun.toString()
            binding.tvNamaUser.text = vgmWithUserModel.namaUser
            binding.tvTimestamp.text = vgmWithUserModel.latestTimestamp.toString()

            Glide.with(binding.ivVgmImage.context)
                .load(Uri.parse(vgmWithUserModel.latestFoto))
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.ivVgmImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVgmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VgmWithUserModel>() {
            override fun areItemsTheSame(
                oldItem: VgmWithUserModel,
                newItem: VgmWithUserModel
            ): Boolean {
                return oldItem.idBibit == newItem.idBibit
            }

            override fun areContentsTheSame(
                oldItem: VgmWithUserModel,
                newItem: VgmWithUserModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
