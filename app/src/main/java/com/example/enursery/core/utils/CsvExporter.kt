package com.example.enursery.core.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.example.enursery.core.domain.model.PlotWithBarisModel
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.time.format.DateTimeFormatter
import java.util.Locale

object CsvExporter {

    @RequiresApi(Build.VERSION_CODES.Q)
    fun exportPlotWithBarisToCsv(context: Context, plot: PlotWithBarisModel): Uri? {
        return try {
            val filename = "id_bibit_${plot.idPlot}.csv"
            val resolver = context.contentResolver

            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, filename)
                put(MediaStore.Downloads.MIME_TYPE, "text/csv")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                resolver.openOutputStream(it)?.use { output ->
                    val writer = BufferedWriter(OutputStreamWriter(output))

                    // Header
                    writer.write("ID Plot;Nama Plot;Varietas;Luas Area (ha);Lat;Lng;Tanggal Tanam;Tanggal Transplantasi;Nama Baris;ID Bibit")
                    writer.newLine()

                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale("id", "ID"))

                    var totalIdBibit = 0

                    for (baris in plot.barisList) {
                        val idList = IdGenerator.generateIdBibitPerBaris(
                            plot.idPlot,
                            baris.namaBaris,
                            baris.jumlahTargetVgm
                        )
                        totalIdBibit += idList.size

                        for (id in idList) {
                            writer.write(
                                listOf(
                                    plot.idPlot,
                                    plot.namaPlot,
                                    plot.varietas,
                                    plot.luasArea,
                                    plot.latitude,
                                    plot.longitude,
                                    plot.tanggalTanam.format(formatter),
                                    plot.tanggalTransplantasi.format(formatter),
                                    baris.namaBaris,
                                    id
                                ).joinToString(";")
                            )
                            writer.newLine()
                        }
                    }

                    // Tambahan: baris kosong + ringkasan total
                    writer.newLine()
                    writer.write("Jumlah VGM:;$totalIdBibit")
                    writer.newLine()

                    writer.flush()
                }
            }

            uri
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveToDownloads(context: Context, fileName: String, content: String): Uri? {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "text/csv")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                OutputStreamWriter(outputStream, StandardCharsets.UTF_8).use { writer ->
                    writer.write(content)
                    writer.flush()
                }
            }
        }

        return uri
    }
}