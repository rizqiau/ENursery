package com.example.enursery.core.utils.mapper

import com.example.enursery.core.data.source.local.entity.PlotEntity
import com.example.enursery.core.data.source.local.entity.PlotWithBaris
import com.example.enursery.core.data.source.local.entity.PlotWithVgmCount
import com.example.enursery.core.data.source.remote.response.PlotResponse
import com.example.enursery.core.domain.model.Plot
import com.example.enursery.core.domain.model.PlotWithBarisModel
import com.example.enursery.core.domain.model.PlotWithVgmCountModel

object PlotMapper {

    fun mapPlotResponseToEntities(input: List<PlotResponse>): List<PlotEntity> {
        return input.map {
            PlotEntity(
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

    fun mapPlotWithBarisToDomain(input: PlotWithBaris): PlotWithBarisModel {
        return PlotWithBarisModel(
            idPlot = input.plot.idPlot,
            namaPlot = input.plot.namaPlot,
            luasArea = input.plot.luasArea,
            varietas = input.plot.varietas,
            tanggalTanam = input.plot.tanggalTanam,
            tanggalTransplantasi = input.plot.tanggalTransplantasi,
            latitude = input.plot.latitude,
            longitude = input.plot.longitude,
            jumlahBibit = input.plot.jumlahBibit,
            barisList = input.barisList.map { BarisMapper.mapEntityToDomain(it) }
        )
    }
}