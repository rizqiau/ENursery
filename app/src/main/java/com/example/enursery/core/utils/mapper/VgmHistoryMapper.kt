package com.example.enursery.core.utils.mapper

import com.example.enursery.core.data.source.local.entity.VgmHistoryEntity
import com.example.enursery.core.domain.model.VgmHistory

object VgmHistoryMapper {

    fun mapEntityToDomain(entity: VgmHistoryEntity): VgmHistory {
        return VgmHistory(
            id = entity.id,
            idBibit = entity.idBibit,
            idPlot = entity.idPlot,
            idUser = entity.idUser,
            idBatch = entity.idBatch,
            tinggi = entity.tinggi,
            diameter = entity.diameter,
            jumlahDaun = entity.jumlahDaun,
            tanggalInput = entity.tanggalInput,
            foto = entity.foto
        )
    }

    fun mapDomainToEntity(domain: VgmHistory): VgmHistoryEntity {
        return VgmHistoryEntity(
            id = domain.id,
            idBibit = domain.idBibit,
            idPlot = domain.idPlot,
            idUser = domain.idUser,
            idBatch = domain.idBatch,
            tinggi = domain.tinggi,
            diameter = domain.diameter,
            jumlahDaun = domain.jumlahDaun,
            tanggalInput = domain.tanggalInput,
            foto = domain.foto
        )
    }
}
