package com.example.enursery.core.utils.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.enursery.core.data.source.local.entity.BatchEntity
import com.example.enursery.core.data.source.remote.response.BatchResponse
import com.example.enursery.core.domain.model.Batch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object BatchMapper {
    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale("id", "ID"))

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapResponsesToEntities(input: List<BatchResponse>): List<BatchEntity> {
        return input.map {
            BatchEntity(
                idBatch = it.idBatch,
                namaBatch = it.namaBatch,
                tanggalMulai = LocalDate.parse(it.tanggalMulai, formatter),
                tanggalSelesai = LocalDate.parse(it.tanggalSelesai, formatter)
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
