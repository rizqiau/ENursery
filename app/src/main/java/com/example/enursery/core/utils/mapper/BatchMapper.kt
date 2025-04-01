package com.example.enursery.core.utils.mapper

import com.example.enursery.core.data.source.local.entity.BatchEntity
import com.example.enursery.core.data.source.remote.response.BatchResponse
import com.example.enursery.core.domain.model.Batch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object BatchMapper {
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))

    fun mapResponsesToEntities(input: List<BatchResponse>): List<BatchEntity> {
        return input.map {
            BatchEntity(
                idBatch = it.idBatch,
                namaBatch = it.namaBatch,
                tanggalMulai = formatter.parse(it.tanggalMulai) ?: Date(),
                tanggalSelesai = formatter.parse(it.tanggalSelesai) ?: Date()
            )
        }
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
