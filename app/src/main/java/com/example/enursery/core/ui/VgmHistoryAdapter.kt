package com.example.enursery.core.ui

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.enursery.R
import com.example.enursery.core.domain.model.VgmHistory
import com.example.enursery.databinding.ItemVgmHistoryBinding
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class VgmHistoryAdapter :
    ListAdapter<VgmHistory, VgmHistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemVgmHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
        animateFadeIn(holder.binding.root)
    }

    private fun animateFadeIn(view: View) {
        view.alpha = 0f
        view.animate()
            .alpha(1f)
            .setDuration(400)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    inner class HistoryViewHolder(val binding: ItemVgmHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: VgmHistory) {
            binding.tvNamaUser.text = "Diinput oleh: ${data.namaUser}"
            binding.tvTinggiTanaman.text = "Tinggi: ${data.tinggi} cm"
            binding.tvDiameterBatang.text = "Diameter: ${data.diameter} cm"
            binding.tvJumlahDaun.text = "Pelepah: ${data.jumlahDaun} Helai"
            binding.tvLebarPetiole.text = "Lebar: ${data.lebarPetiole} cm"
            binding.tvStatus.text = "Status: ${data.status}" // ✅ Tambah status

            // Format tanggal + jam input
            val formatter = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm", Locale("id"))
            val formattedDate = Instant.ofEpochMilli(data.createdAt)
                .atZone(ZoneId.systemDefault()).toLocalDateTime().format(formatter)
            binding.tvTanggalInput.text = formattedDate

            // Aksi lihat foto
            binding.tvLihatFoto.setOnClickListener {
                showImageDialog(binding.root.context, data.foto) // ✅ fix context
            }
        }
    }

    private fun showImageDialog(context: Context, fotoUri: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_image_preview, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.ivPreview)

        Glide.with(context)
            .load(Uri.parse(fotoUri))
            .placeholder(R.drawable.ic_placeholder)
            .into(imageView)

        AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(true)
            .show()
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VgmHistory>() {
            override fun areItemsTheSame(oldItem: VgmHistory, newItem: VgmHistory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: VgmHistory, newItem: VgmHistory): Boolean {
                return oldItem == newItem
            }
        }
    }
}
