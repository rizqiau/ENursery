package com.example.enursery.core.utils.mapper

import com.example.enursery.core.data.source.local.entity.VgmEntity
import com.example.enursery.core.data.source.local.entity.VgmHistoryEntity
import com.example.enursery.core.data.source.local.entity.VgmWithUser
import com.example.enursery.core.data.source.remote.response.VgmResponse
import com.example.enursery.core.domain.model.Vgm
import com.example.enursery.core.domain.model.VgmWithUserModel
import java.time.LocalDate

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
                latestLebarPetiole = it.latestLebarPetiole,
                latestTanggalInput = it.latestTanggalInput, // LocalDate assumed already
                latestFoto = it.latestFoto,
                createdAt = it.createdAt // pastikan Response punya ini juga
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
            latestLebarPetiole = entity.latestLebarPetiole,
            latestTanggalInput = entity.latestTanggalInput?.let { LocalDate.ofEpochDay(it) },
            latestFoto = entity.latestFoto,
            createdAt = entity.createdAt
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
            latestLebarPetiole = domain.latestLebarPetiole,
            latestTanggalInput = domain.latestTanggalInput?.toEpochDay(),
            latestFoto = domain.latestFoto,
            createdAt = domain.createdAt
        )
    }

    fun mapDomainToEntities(domains: List<Vgm>): List<VgmEntity> =
        domains.map { mapDomainToEntity(it) }

    // ------------------------------
    // @Relation Vgm + User → Domain model
    // ------------------------------
    fun mapVgmWithUserToModel(input: List<VgmWithUser>): List<VgmWithUserModel> {
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
                latestLebarPetiole = it.vgm.latestLebarPetiole,
                latestTanggalInput = it.vgm.latestTanggalInput?.let { epoch -> LocalDate.ofEpochDay(epoch) }, // ✅ safe
                latestFoto = it.vgm.latestFoto,
                namaUser = it.user?.namaUser ?: "_",
                createdAt = it.vgm.createdAt
            )
        }
    }

    // ------------------------------
    // Convert 1 VgmHistoryEntity → VgmEntity (snapshot update)
    // ------------------------------
    fun mapHistoryToVgmEntity(history: VgmHistoryEntity): VgmEntity {
        return VgmEntity(
            idBibit = history.idBibit,
            idPlot = history.idPlot,
            idBaris = history.idBaris,
            idUser = history.idUser,
            idBatch = history.idBatch,
            status = history.status,
            latestTinggiTanaman = history.tinggi,
            latestDiameterBatang = history.diameter,
            latestJumlahDaun = history.jumlahDaun,
            latestLebarPetiole = history.lebarPetiole,
            latestTanggalInput = history.tanggalInput,
            latestFoto = history.foto,
            createdAt = history.createdAt
        )
    }

    fun mapHistoryToDomain(history: VgmHistoryEntity): Vgm {
        return mapEntityToDomain(mapHistoryToVgmEntity(history))
    }

    fun mapHistoriesToDomain(histories: List<VgmHistoryEntity>): List<Vgm> {
        return histories.map { mapHistoryToDomain(it) }
    }
}
