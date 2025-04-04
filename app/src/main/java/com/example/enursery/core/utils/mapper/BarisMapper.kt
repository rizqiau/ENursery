package com.example.enursery.core.utils.mapper

import com.example.enursery.core.data.source.local.entity.BarisEntity
import com.example.enursery.core.domain.model.Baris

object BarisMapper {

    fun mapDomainToEntity(baris: Baris): BarisEntity {
        return BarisEntity(
            idBaris = baris.idBaris,
            idPlot = baris.idPlot,
            namaBaris = baris.namaBaris,
            jumlahTargetVgm = baris.jumlahTargetVgm
        )
    }

    fun mapEntityToDomain(entity: BarisEntity): Baris {
        return Baris(
            idBaris = entity.idBaris,
            idPlot = entity.idPlot,
            namaBaris = entity.namaBaris,
            jumlahTargetVgm = entity.jumlahTargetVgm
        )
    }

    fun mapEntitiesToDomain(list: List<BarisEntity>): List<Baris> =
        list.map { mapEntityToDomain(it) }

    fun mapDomainToEntities(list: List<Baris>): List<BarisEntity> =
        list.map { mapDomainToEntity(it) }
}
