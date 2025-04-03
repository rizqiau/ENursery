package com.example.enursery.presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.example.enursery.R
import com.example.enursery.core.domain.model.PlotWithVgmCountModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class PlotNavigator(private val context: Context, private val layoutInflater: LayoutInflater) {
    fun show(plot: PlotWithVgmCountModel) {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_plot, null)
        val dialog = BottomSheetDialog(context).apply { setContentView(view) }

        view.findViewById<TextView>(R.id.tvPlotTitle).text = plot.namaPlot
        view.findViewById<TextView>(R.id.tvPlotInfo).text =
            "Luas: ${plot.luasArea} ha\nJumlah VGM: ${plot.jumlahVgm}"

        view.findViewById<Button>(R.id.btnNavigate).setOnClickListener {
            dialog.dismiss()
            val uri = Uri.parse("google.navigation:q=${plot.latitude},${plot.longitude}")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage("com.google.android.apps.maps")
            }
            if (intent.resolveActivity(context.packageManager) != null) context.startActivity(intent)
        }

        dialog.show()
    }
}
