package com.example.enursery.core.utils.mapper

import com.example.enursery.core.data.source.local.entity.VgmDailyStatEntity
import com.example.enursery.core.data.source.local.entity.VgmEntity
import com.example.enursery.core.data.source.local.entity.VgmHistoryEntity
import com.example.enursery.core.domain.model.VgmDailyStat
import com.example.enursery.core.domain.model.VgmHistory
import java.time.LocalDate

object VgmHistoryMapper {

    // ------------------------------
    // Entity → Domain
    // ------------------------------
    fun mapEntityToDomain(entity: VgmHistoryEntity): VgmHistory {
        return VgmHistory(
            id = entity.id,
            idBibit = entity.idBibit,
            idPlot = entity.idPlot,
            idBaris = entity.idBaris,
            idUser = entity.idUser,
            namaUser = entity.namaUser,
            idBatch = entity.idBatch,
            status = entity.status,
            tinggi = entity.tinggi,
            diameter = entity.diameter,
            jumlahDaun = entity.jumlahDaun,
            lebarPetiole = entity.lebarPetiole,
            tanggalInput = LocalDate.ofEpochDay(entity.tanggalInput),
            foto = entity.foto,
            createdAt = entity.createdAt
        )
    }

    fun mapEntitiesToDomain(entities: List<VgmHistoryEntity>): List<VgmHistory> =
        entities.map { mapEntityToDomain(it) }

    // ------------------------------
    // Domain → Entity
    // ------------------------------
    fun mapDomainToEntity(domain: VgmHistory): VgmHistoryEntity {
        return VgmHistoryEntity(
            id = domain.id,
            idBibit = domain.idBibit,
            idPlot = domain.idPlot,
            idBaris = domain.idBaris,
            idUser = domain.idUser,
            namaUser = domain.namaUser,
            idBatch = domain.idBatch,
            status = domain.status,
            tinggi = domain.tinggi,
            diameter = domain.diameter,
            jumlahDaun = domain.jumlahDaun,
            lebarPetiole = domain.lebarPetiole,
            tanggalInput = domain.tanggalInput.toEpochDay(),
            foto = domain.foto,
            createdAt = domain.createdAt
        )
    }

    fun mapDomainsToEntity(domains: List<VgmHistory>): List<VgmHistoryEntity> =
        domains.map { mapDomainToEntity(it) }

    // ------------------------------
    // Domain → Snapshot VGMEntity
    // ------------------------------
    fun mapHistoryToVgmEntity(history: VgmHistory): VgmEntity {
        return VgmEntity(
            idBibit = history.idBibit,
            idPlot = history.idPlot,
            idBaris = history.idBaris,
            idUser = history.idUser,
            status = history.status,
            idBatch = history.idBatch,
            latestTinggiTanaman = history.tinggi,
            latestDiameterBatang = history.diameter,
            latestJumlahDaun = history.jumlahDaun,
            latestLebarPetiole = history.lebarPetiole,
            latestTanggalInput = history.tanggalInput.toEpochDay(),
            createdAt = history.createdAt,
            latestFoto = history.foto
        )
    }

    // ------------------------------
    // Entity → Domain (Statistik Harian)
    // ------------------------------
    fun mapDailyStatEntitiesToDomain(entities: List<VgmDailyStatEntity>): List<VgmDailyStat> {
        return entities.map {
            VgmDailyStat(
                tanggal = it.tanggal,
                jumlahInput = it.jumlahInput
            )
        }
    }
}
