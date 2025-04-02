package com.example.enursery.core.utils.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.enursery.core.data.source.local.entity.PlotEntity
import com.example.enursery.core.data.source.local.entity.PlotWithVgmCount
import com.example.enursery.core.data.source.remote.response.PlotResponse
import com.example.enursery.core.domain.model.Plot
import com.example.enursery.core.domain.model.PlotWithVgmCountModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object PlotMapper {
    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale("id", "ID"))

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapPlotResponseToEntities(input: List<PlotResponse>): List<PlotEntity> {
        return input.map {
            PlotEntity(
                idPlot = it.idPlot,
                namaPlot = it.namaPlot,
                luasArea = it.luasArea,
                tanggalTanam = LocalDate.parse(it.tanggalTanam, formatter),
                tanggalTransplantasi = LocalDate.parse(it.tanggalTransplantasi, formatter),
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
        return input.map {
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

    fun mapPlotDomainToEntity(plot: Plot): PlotEntity {
        return PlotEntity(
            idPlot = plot.idPlot,
            namaPlot = plot.namaPlot,
            luasArea = plot.luasArea,
            tanggalTanam = plot.tanggalTanam,
            tanggalTransplantasi = plot.tanggalTransplantasi,
            varietas = plot.varietas,
            latitude = plot.latitude,
            longitude = plot.longitude,
            jumlahBibit = plot.jumlahBibit
        )
    }
}