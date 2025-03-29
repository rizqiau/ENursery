package com.example.enursery.core.utils

import android.util.Log
import com.example.enursery.core.data.source.local.entity.PlotEntity
import com.example.enursery.core.data.source.local.entity.PlotWithVgmCount
import com.example.enursery.core.data.source.local.entity.UserEntity
import com.example.enursery.core.data.source.local.entity.VgmEntity
import com.example.enursery.core.data.source.remote.response.PlotResponse
import com.example.enursery.core.data.source.remote.response.UserResponse
import com.example.enursery.core.data.source.remote.response.VgmResponse
import com.example.enursery.core.domain.model.Plot
import com.example.enursery.core.domain.model.PlotWithVgmCountModel
import com.example.enursery.core.domain.model.User
import com.example.enursery.core.domain.model.Vgm
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DataMapper {
    fun mapUserResponseToUserEntities(input: List<UserResponse>): List<UserEntity> {
        val userList = ArrayList<UserEntity>()
        input.map {
            val user = UserEntity(
                id = it.id,
                nama = it.nama,
                roleId = it.roleId,
                wilayahId = it.wilayahId,
                foto = it.foto,
                email = it.email,
                password = it.password
            )
            userList.add(user)
        }
        return userList
    }

    fun mapUserEntitiesToUserDomain(input: List<UserEntity>): List<User> =
        input.map {
            User(
                id = it.id,
                nama = it.nama,
                role = it.roleId,
                wilayahKerja = it.wilayahId,
                foto = it.foto,
                email = it.email,
                password = it.password
            )
        }


    fun mapUserDomainToUserEntity(input: User) = UserEntity(
        id = input.id,
        nama = input.nama,
        roleId = input.role,
        wilayahId = input.wilayahKerja,
        foto = input.foto,
        email = input.email,
        password = input.password
    )

    fun mapUserEntityToDomain(input: UserEntity): User = User(
        id = input.id,
        nama = input.nama,
        role = input.roleId,
        wilayahKerja = input.wilayahId,
        foto = input.foto,
        email = input.email,
        password = input.password
    )

    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun mapPlotResponseToEntities(input: List<PlotResponse>): List<PlotEntity> {
        return input.map {
            PlotEntity(
                idPlot = it.idPlot,
                namaPlot = it.namaPlot,
                luasArea = it.luasArea,
                tanggalTanam = formatter.parse(it.tanggalTanam) ?: Date(),
                tanggalTransplantasi = formatter.parse(it.tanggalTransplantasi) ?: Date(),
                varietas = it.varietas,
                latitude = it.latitude,
                longitude = it.longitude,
                jumlahBibit = it.jumlahBibit
            )
        }
    }

    fun mapPlotEntitiesToDomain(input: List<PlotEntity>): List<Plot> =
        input.map {
            Plot(
                idPlot = it.idPlot,
                namaPlot = it.namaPlot,
                luasArea = it.luasArea,
                tanggalTanam = it.tanggalTanam,
                tanggalTransplantasi = it.tanggalTransplantasi,
                varietas = it.varietas,
                latitude = it.latitude,
                longitude = it.longitude,
                jumlahBibit = it.jumlahBibit
            )
        }

    fun mapPlotWithVgmEntityToDomain(input: List<PlotWithVgmCount>): List<PlotWithVgmCountModel> {
        Log.d("DataMapper", "Mapping ${input.size} plot with vgm")
        return input.map {
            Log.d("DataMapper", "Plot ${it.plot.idPlot} -> ${it.jumlahVgm}")
            PlotWithVgmCountModel(
                idPlot = it.plot.idPlot,
                namaPlot = it.plot.namaPlot,
                luasArea = it.plot.luasArea,
                tanggalTanam = it.plot.tanggalTanam,
                tanggalTransplantasi = it.plot.tanggalTransplantasi,
                varietas = it.plot.varietas,
                latitude = it.plot.latitude,
                longitude = it.plot.longitude,
                jumlahBibit = it.plot.jumlahBibit,
                jumlahVgm = it.jumlahVgm
            )
        }
    }

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
}