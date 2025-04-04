package com.example.enursery.core.utils.mapper

import com.example.enursery.core.data.source.local.entity.BatchEntity
import com.example.enursery.core.data.source.remote.response.BatchResponse
import com.example.enursery.core.domain.model.Batch

object BatchMapper {

    fun mapResponsesToEntities(input: List<BatchResponse>): List<BatchEntity> {
        return input.map {
            BatchEntity(
                idBatch = it.idBatch,
                namaBatch = it.namaBatch,
                tanggalMulai = it.tanggalMulai,
                tanggalSelesai = it.tanggalSelesai
            )
        }
    }

    fun mapDomainToEntities(batch: Batch): BatchEntity{
        return BatchEntity(
            idBatch = batch.idBatch,
            namaBatch = batch.namaBatch,
            tanggalMulai = batch.tanggalMulai,
            tanggalSelesai = batch.tanggalSelesai
        )
    }

    fun mapEntitiesToDomain(input: List<BatchEntity>): List<Batch> {
        return input.map {
            Batch(
                idBatch = it.idBatch,
                namaBatch = it.namaBatch,
                tanggalMulai = it.tanggalMulai,
                tanggalSelesai = it.tanggalSelesai
            )
        }
    }
}
