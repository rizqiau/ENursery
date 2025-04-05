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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class VgmAdapter(
    private val onItemClick: (VgmWithUserModel) -> Unit
) : ListAdapter<VgmWithUserModel, VgmAdapter.ViewHolder>(DIFF_CALLBACK) {


    val timeZoneId = TimeZone.getDefault().id
    val zoneLabel = when (timeZoneId) {
        "Asia/Jakarta" -> "WIB"
        "Asia/Makassar" -> "WITA"
        "Asia/Jayapura" -> "WIT"
        else -> "Local"
    }

    inner class ViewHolder(private val binding: ItemVgmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vgmWithUserModel: VgmWithUserModel) {
            Log.d("VgmAdapter", "Bind ${vgmWithUserModel.idBibit}")
            binding.tvBibitId.text = vgmWithUserModel.idBibit
            binding.tvTinggiTanaman.text = "${vgmWithUserModel.latestTinggiTanaman} CM"
            binding.tvDiameterBatang.text = "${vgmWithUserModel.latestDiameterBatang} CM"
            binding.tvJumlahDaun.text = "${vgmWithUserModel.latestJumlahDaun} CM"
            binding.tvNamaUser.text = vgmWithUserModel.namaUser
            binding.tvTimestamp.text =
                vgmWithUserModel.createdAt?.let { formatTanggalDenganZona(it, zoneLabel) }


            // Set image bibit
            Glide.with(binding.ivVgmImage.context)
                .load(Uri.parse(vgmWithUserModel.latestFoto))
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.ivVgmImage)

            // Set status icon based on status
            val statusIcon = when (vgmWithUserModel.status) {
                "AKTIF" -> R.drawable.ic_status_active
                "MATI" -> R.drawable.ic_status_inactive
                else -> R.drawable.ic_status_unknown
            }
            binding.ivStatusIcon.setImageResource(statusIcon)
            binding.root.setOnClickListener {
                onItemClick(vgmWithUserModel)
            }
        }
    }

    fun formatTanggalDenganZona(timestamp: Long, zona: String): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("d MMM yyyy, HH:mm", Locale("id", "ID"))

        // Set zona waktu sesuai kode
        when (zona.uppercase()) {
            "WIB" -> formatter.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
            "WITA" -> formatter.timeZone = TimeZone.getTimeZone("Asia/Makassar")
            "WIT" -> formatter.timeZone = TimeZone.getTimeZone("Asia/Jayapura")
            else -> formatter.timeZone = TimeZone.getDefault() // fallback
        }

        return "${formatter.format(date)} $zona"
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
