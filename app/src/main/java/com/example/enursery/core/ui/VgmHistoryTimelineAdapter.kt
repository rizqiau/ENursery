package com.example.enursery.core.ui

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.enursery.R
import com.example.enursery.core.domain.model.VgmHistory
import com.example.enursery.core.utils.DateFormatter
import com.example.enursery.databinding.ItemTimelineBinding
import com.github.vipulasri.timelineview.TimelineView

class VgmHistoryTimelineAdapter(
    private val data: List<VgmHistory>
) : RecyclerView.Adapter<VgmHistoryTimelineAdapter.TimelineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val binding = ItemTimelineBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TimelineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        holder.bind(data[position], viewType)
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, data.size)
    }

    inner class TimelineViewHolder(
        private val binding: ItemTimelineBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: VgmHistory, viewType: Int) = with(binding) {
            timelineView.initLine(viewType)

            tvTitle.text = "Diinput oleh: ${item.namaUser}"
            tvTanggalInput.text = DateFormatter.formatTanggalDanWaktuFromMillis(item.createdAt)
            tvTinggiTanaman.text = "Tinggi: ${item.tinggi} cm"
            tvDiameterBatang.text = "Diameter: ${item.diameter} cm"
            tvJumlahDaun.text = "Pelepah: ${item.jumlahDaun} helai"
            tvLebarPetiole.text = "Lebar: ${item.lebarPetiole} cm"

            val context = root.context
            val statusIcon = when (item.status.uppercase()) {
                "AKTIF" -> R.drawable.ic_status_active
                "MATI" -> R.drawable.ic_status_inactive
                else -> R.drawable.ic_status_unknown
            }
            ivStatusIcon.setImageResource(statusIcon)

            tvLihatFoto.setOnClickListener {
                showImageDialog(context, item.foto)
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
    }
}

