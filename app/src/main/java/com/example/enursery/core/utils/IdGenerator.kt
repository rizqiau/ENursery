package com.example.enursery.core.utils

object IdGenerator {

    fun generateBatchIdAndName(input: String): Pair<String, String> {
        // Ambil angka dari input
        val regex = "(\\d+)".toRegex()
        val match = regex.find(input)

        val angka = match?.value ?: "0" // default kalau gak ada angka

        val idBatch = "B_${angka.toInt()}" // hilangkan leading zero
        val namaBatch = "BATCH ${angka.toInt()}" // sama, biar konsisten

        return Pair(idBatch, namaBatch)
    }

    fun generateBarisId(idPlot: String, namaBaris: String): String {
        val kodePlot = PlotNameFormatter.extractKodePlot(idPlot)
        return "$kodePlot-$namaBaris"
    }

    fun generateIdBibitPerBaris(idPlot: String, namaBaris: String, jumlahTargetVgm: Int): List<String> {
        val barisId = generateBarisId(idPlot, namaBaris)
        return (1..jumlahTargetVgm).map { nomor ->
            val nomorFormatted = nomor.toString().padStart(2, '0')
            "$barisId-$nomorFormatted"
        }
    }
}
