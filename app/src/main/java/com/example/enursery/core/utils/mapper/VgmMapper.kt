package com.example.enursery.core.utils.mapper

import android.util.Log
import com.example.enursery.core.data.source.local.entity.VgmEntity
import com.example.enursery.core.data.source.local.entity.VgmHistoryEntity
import com.example.enursery.core.data.source.local.entity.VgmWithUser
import com.example.enursery.core.data.source.remote.response.VgmResponse
import com.example.enursery.core.domain.model.Vgm
import com.example.enursery.core.domain.model.VgmWithUserModel

object VgmMapper {

    // ------------------------------
    // Seeder / API → Room
    // ------------------------------
    fun mapVgmResponseToEntities(input: List<VgmResponse>): List<VgmEntity> {
        return input.map {
            VgmEntity(
                idBibit = it.idBibit,
                idPlot = it.idPlot,
                idBaris = it.idBaris,
                idUser = it.idUser,
                idBatch = it.idBatch,
                status = it.status,
                latestTinggiTanaman = it.latestTinggiTanaman,
                latestDiameterBatang = it.latestDiameterBatang,
                latestJumlahDaun = it.latestJumlahDaun,
                latestTanggalInput = it.latestTanggalInput, // fallback jika parsing gagal
                latestFoto = it.latestFoto,
                latestTimestamp = it.latestTimestamp
            )
        }
    }

    // ------------------------------
    // Room → Domain
    // ------------------------------
    fun mapEntityToDomain(entity: VgmEntity): Vgm {
        return Vgm(
            idBibit = entity.idBibit,
            idPlot = entity.idPlot,
            idBaris = entity.idBaris,
            idUser = entity.idUser,
            idBatch = entity.idBatch,
            status = entity.status,
            latestTinggiTanaman = entity.latestTinggiTanaman,
            latestDiameterBatang = entity.latestDiameterBatang,
            latestJumlahDaun = entity.latestJumlahDaun,
            latestTanggalInput = entity.latestTanggalInput,
            latestFoto = entity.latestFoto,
            latestTimestamp = entity.latestTimestamp
        )
    }

    fun mapEntitiesToDomain(entities: List<VgmEntity>): List<Vgm> =
        entities.map { mapEntityToDomain(it) }

    // ------------------------------
    // Domain → Room
    // ------------------------------
    fun mapDomainToEntity(domain: Vgm): VgmEntity {
        return VgmEntity(
            idBibit = domain.idBibit,
            idPlot = domain.idPlot,
            idBaris = domain.idBaris,
            idUser = domain.idUser,
            idBatch = domain.idBatch,
            status = domain.status,
            latestTinggiTanaman = domain.latestTinggiTanaman,
            latestDiameterBatang = domain.latestDiameterBatang,
            latestJumlahDaun = domain.latestJumlahDaun,
            latestTanggalInput = domain.latestTanggalInput,
            latestFoto = domain.latestFoto,
            latestTimestamp = domain.latestTimestamp
        )
    }

    fun mapDomainToEntities(domains: List<Vgm>): List<VgmEntity> {
        return domains.map { mapDomainToEntity(it) }
    }

    // ------------------------------
    // @Relation Vgm + User → Domain model
    // ------------------------------
    fun mapVgmWithUserToModel(input: List<VgmWithUser>): List<VgmWithUserModel> {
        Log.d("VgmMapper", "Mapping VgmWithUser → jumlah data: ${input.size}")
        return input.map {
            VgmWithUserModel(
                idBibit = it.vgm.idBibit,
                idPlot = it.vgm.idPlot,
                idUser = it.vgm.idUser,
                idBatch = it.vgm.idBatch,
                status = it.vgm.status,
                latestTinggiTanaman = it.vgm.latestTinggiTanaman,
                latestDiameterBatang = it.vgm.latestDiameterBatang,
                latestJumlahDaun = it.vgm.latestJumlahDaun,
                latestTanggalInput = it.vgm.latestTanggalInput,
                latestFoto = it.vgm.latestFoto,
                namaUser = it.user?.namaUser ?: "_",
                latestTimestamp = it.vgm.latestTimestamp
            )
        }
    }

    // ------------------------------
    // Convert 1 VgmHistoryEntity → VgmEntity (snapshot update)
    // ------------------------------
    fun mapHistoryToVgmEntity(history: VgmHistoryEntity): VgmEntity {return VgmEntity(
            idBibit = history.idBibit,
            idPlot = history.idPlot,
            idBaris = history.idBaris,
            idUser = history.idUser,
            idBatch = history.idBatch,
            status = history.status, // default, bisa dikembangkan sesuai status real
            latestTinggiTanaman = history.tinggi,
            latestDiameterBatang = history.diameter,
            latestJumlahDaun = history.jumlahDaun,
            latestTanggalInput = history.tanggalInput,
            latestFoto = history.foto,
            latestTimestamp = System.currentTimeMillis()
        )
    }

    fun mapHistoryToDomain(history: VgmHistoryEntity): Vgm {
        return mapEntityToDomain(mapHistoryToVgmEntity(history))
    }

    fun mapHistoriesToDomain(histories: List<VgmHistoryEntity>): List<Vgm> {
        return histories.map { mapHistoryToDomain(it) }
    }
}

