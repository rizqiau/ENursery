package com.example.enursery.core.utils.mapper

import com.example.enursery.core.data.source.local.entity.VgmEntity
import com.example.enursery.core.data.source.local.entity.VgmHistoryEntity
import com.example.enursery.core.domain.model.VgmHistory

object VgmHistoryMapper {

    // ------------------------------
    // Entity → Domain
    // ------------------------------
    fun mapEntityToDomain(entity: VgmHistoryEntity): VgmHistory {
        return VgmHistory(
            id = entity.id,
            idBibit = entity.idBibit,
            idPlot = entity.idPlot,
            idUser = entity.idUser,
            idBatch = entity.idBatch,
            status = entity.status,
            tinggi = entity.tinggi,
            diameter = entity.diameter,
            jumlahDaun = entity.jumlahDaun,
            tanggalInput = entity.tanggalInput,
            foto = entity.foto
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
            idUser = domain.idUser,
            idBatch = domain.idBatch,
            status = domain.status,
            tinggi = domain.tinggi,
            diameter = domain.diameter,
            jumlahDaun = domain.jumlahDaun,
            tanggalInput = domain.tanggalInput,
            foto = domain.foto
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
            idUser = history.idUser,
            status = history.status,
            idBatch = history.idBatch,
            latestTinggiTanaman = history.tinggi,
            latestDiameterBatang = history.diameter,
            latestJumlahDaun = history.jumlahDaun,
            latestTanggalInput = history.tanggalInput,
            latestTimestamp = System.currentTimeMillis(), // waktu update snapshot
            latestFoto = history.foto
        )
    }
}

