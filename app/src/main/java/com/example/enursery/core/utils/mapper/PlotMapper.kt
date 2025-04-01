package com.example.enursery.core.utils.mapper

import android.util.Log
import com.example.enursery.core.data.source.local.entity.PlotEntity
import com.example.enursery.core.data.source.local.entity.PlotWithVgmCount
import com.example.enursery.core.data.source.remote.response.PlotResponse
import com.example.enursery.core.domain.model.Plot
import com.example.enursery.core.domain.model.PlotWithVgmCountModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PlotMapper {
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))

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