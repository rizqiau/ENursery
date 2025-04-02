package com.example.enursery.core.utils.mapper

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.enursery.core.data.source.local.entity.VgmEntity
import com.example.enursery.core.data.source.local.entity.VgmHistoryEntity
import com.example.enursery.core.data.source.local.entity.VgmWithUser
import com.example.enursery.core.data.source.remote.response.VgmResponse
import com.example.enursery.core.domain.model.Vgm
import com.example.enursery.core.domain.model.VgmWithUserModel
import com.example.enursery.core.utils.DateUtils
import java.time.LocalDate
import java.time.ZoneId

object VgmMapper {

    // ------------------------------
    // Seeder / API → Room
    // ------------------------------
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapVgmResponseToEntities(input: List<VgmResponse>): List<VgmEntity> {
        return input.map {
            val tanggal = DateUtils.parseLocalDate(it.latestTanggalInput) ?: LocalDate.now()
            val timestamp = tanggal.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            VgmEntity(
                idBibit = it.idBibit,
                idPlot = it.idPlot,
                idUser = it.idUser,
                idBatch = it.idBatch,
                status = it.status,
                latestTinggiTanaman = it.latestTinggiTanaman,
                latestDiameterBatang = it.latestDiameterBatang,
                latestJumlahDaun = it.latestJumlahDaun,
                latestTanggalInput = tanggal, // fallback jika parsing gagal
                latestFoto = it.latestFoto,
                latestTimestamp = timestamp
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
                namaUser = it.user.namaUser,
                latestTimestamp = it.vgm.latestTimestamp
            )
        }
    }

    // ------------------------------
    // Convert 1 VgmHistoryEntity → VgmEntity (snapshot update)
    // ------------------------------
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapHistoryToVgmEntity(history: VgmHistoryEntity): VgmEntity {
        val timestamp = history.tanggalInput.atStartOfDay(ZoneId.systemDefault())
            .toInstant().toEpochMilli()

        return VgmEntity(
            idBibit = history.idBibit,
            idPlot = history.idPlot,
            idUser = history.idUser,
            idBatch = history.idBatch,
            status = "Aktif", // default, bisa dikembangkan sesuai status real
            latestTinggiTanaman = history.tinggi,
            latestDiameterBatang = history.diameter,
            latestJumlahDaun = history.jumlahDaun,
            latestTanggalInput = history.tanggalInput,
            latestFoto = history.foto,
            latestTimestamp = System.currentTimeMillis()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapHistoryToDomain(history: VgmHistoryEntity): Vgm {
        return mapEntityToDomain(mapHistoryToVgmEntity(history))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapHistoriesToDomain(histories: List<VgmHistoryEntity>): List<Vgm> {
        return histories.map { mapHistoryToDomain(it) }
    }
}

