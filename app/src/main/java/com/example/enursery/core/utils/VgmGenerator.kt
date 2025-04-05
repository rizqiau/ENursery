package com.example.enursery.core.utils

import com.example.enursery.core.domain.model.Baris
import com.example.enursery.core.domain.model.Vgm

// utils/VgmGenerator.kt
object VgmGenerator {
    fun generateFromBarisList(idPlot: String, barisList: List<Baris>): List<Vgm> {
        val result = mutableListOf<Vgm>()

        for (baris in barisList) {
            val idBaris = baris.idBaris
            val idList = IdGenerator.generateIdBibitPerBaris(idPlot, baris.namaBaris, baris.jumlahTargetVgm)
            idList.forEach { idBibit ->
                result.add(
                    Vgm(
                        idBibit = idBibit,
                        idPlot = idPlot,
                        idBaris = idBaris,
                        idUser = null,
                        idBatch = null,
                        status = "AKTIF",
                        latestTinggiTanaman = null,
                        latestDiameterBatang = null,
                        latestJumlahDaun = null,
                        latestLebarPetiole = null,
                        latestTanggalInput = null,
                        latestFoto = null,
                        createdAt = null
                    )
                )
            }
        }

        return result
    }
}

