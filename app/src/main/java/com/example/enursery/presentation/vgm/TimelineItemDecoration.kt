package com.example.enursery.presentation.vgm

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.enursery.R
import com.example.enursery.core.domain.model.VgmHistory

class TimelineItemDecoration(
    private val context: Context,
    private val items: List<VgmHistory>
) : RecyclerView.ItemDecoration() {

    // Ukuran titik dan lebar garis
    private val dotRadius = 12f
    private val lineWidth = 6f
    private val timelineX = 60f

    // Warna default garis vertikal
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.main)
        style = Paint.Style.FILL
    }

    // Fungsi untuk pilih warna titik berdasarkan status
    private fun getDotColor(status: String?): Int {
        return when (status) {
            "AKTIF" -> ContextCompat.getColor(context, R.color.green)
            "MATI" -> ContextCompat.getColor(context, R.color.red)
            else -> ContextCompat.getColor(context, R.color.gray)
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            if (position == RecyclerView.NO_POSITION || position >= items.size) continue

            val item = items[position]
            val statusColor = getDotColor(item.status)

            // Ambil TextView dalam item yang akan dijadikan referensi titik
            val labelView = view.findViewById<TextView>(R.id.tvNamaUser)

            // Hitung Y tengah dari label "Diinput oleh", kalau null fallback ke tengah view
            val centerY = labelView?.let {
                val location = IntArray(2)
                it.getLocationOnScreen(location)
                val labelCenterY = location[1] + it.height / 2f
                val parentLocation = IntArray(2)
                parent.getLocationOnScreen(parentLocation)
                labelCenterY - parentLocation[1]
            } ?: (view.top + view.height / 4f)

            val isFirst = position == 0
            val isLast = position == items.lastIndex
            val lineTop = if (isFirst) centerY else view.top.toFloat()
            val lineBottom = if (isLast) centerY else view.bottom.toFloat()

            // Gambar garis vertikal timeline
            c.drawRect(
                timelineX - (lineWidth / 2),
                lineTop,
                timelineX + (lineWidth / 2),
                lineBottom,
                linePaint
            )

            // Gambar titik status
            val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = statusColor
                style = Paint.Style.FILL
            }
            c.drawCircle(timelineX, centerY.toFloat(), dotRadius, dotPaint)
        }
    }
}

