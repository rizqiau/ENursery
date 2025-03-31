package com.example.enursery.core.utils.mapper

import com.example.enursery.core.data.source.local.entity.VgmEntity
import com.example.enursery.core.data.source.local.entity.VgmWithUser
import com.example.enursery.core.data.source.remote.response.VgmResponse
import com.example.enursery.core.domain.model.Vgm
import com.example.enursery.core.domain.model.VgmWithUserModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object VgmMapper {
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))

    fun mapVgmResponseToEntities(input: List<VgmResponse>): List<VgmEntity> {
        return input.map {
            VgmEntity(
                idBibit = it.idBibit,
                idPlot = it.idPlot,
                idPekerja = it.idPekerja,
                status = it.status,
                latestTinggiTanaman = it.latestTinggiTanaman,
                latestDiameterBatang = it.latestDiameterBatang,
                latestJumlahDaun = it.latestJumlahDaun,
                latestTanggalInput = formatter.parse(it.latestTanggalInput) ?: Date(),
                latestFoto = it.latestFoto
            )
        }
    }

    fun mapVgmEntitiesToDomain(input: List<VgmEntity>): List<Vgm> =
        input.map {
            Vgm(
                idBibit = it.idBibit,
                idPlot = it.idPlot,
                idPekerja = it.idPekerja,
                status = it.status,
                latestTinggiTanaman = it.latestTinggiTanaman,
                latestDiameterBatang = it.latestDiameterBatang,
                latestJumlahDaun = it.latestJumlahDaun,
                latestTanggalInput = it.latestTanggalInput,
                latestFoto = it.latestFoto
            )
        }

    fun mapVgmDomainToEntity(vgm: Vgm): VgmEntity {
        return VgmEntity(
            idBibit = vgm.idBibit,
            idPlot = vgm.idPlot,
            idPekerja = vgm.idPekerja,
            status = vgm.status,
            latestTinggiTanaman = vgm.latestTinggiTanaman,
            latestDiameterBatang = vgm.latestDiameterBatang,
            latestJumlahDaun = vgm.latestJumlahDaun,
            latestTanggalInput = vgm.latestTanggalInput,
            latestFoto = vgm.latestFoto
        )
    }

    fun mapVgmWithUserToModel(input: List<VgmWithUser>): List<VgmWithUserModel> {
        return input.map {
            VgmWithUserModel(
                idBibit = it.vgm.idBibit,
                idPlot = it.vgm.idPlot,
                idPekerja = it.vgm.idPekerja,
                status = it.vgm.status,
                latestTinggiTanaman = it.vgm.latestTinggiTanaman,
                latestDiameterBatang = it.vgm.latestDiameterBatang,
                latestJumlahDaun = it.vgm.latestJumlahDaun,
                latestTanggalInput = it.vgm.latestTanggalInput,
                latestFoto = it.vgm.latestFoto,
                namaUser = it.user.nama
            )
        }
    }
}