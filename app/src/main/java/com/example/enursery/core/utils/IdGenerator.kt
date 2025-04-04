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
        return "${idPlot}-$namaBaris"
    }
}
