package com.example.enursery.core.data.source.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.enursery.core.data.source.local.entity.VgmWithUser
import com.example.enursery.core.data.source.local.room.VgmDao
import com.example.enursery.core.domain.model.PlotProgressModel
import com.example.enursery.core.domain.model.PlotWithVgmCountModel
import com.example.enursery.core.domain.repository.IPlotProgressRepository
import com.example.enursery.core.domain.usecase.user.SessionUseCase
import com.example.enursery.core.utils.mapper.VgmMapper

class PlotProgressRepository(
    private val vgmDao: VgmDao,
    private val userSession: SessionUseCase,
    private val plotRepository: PlotRepository
) : IPlotProgressRepository {

    override fun getPlotProgressByUser(userId: String): LiveData<List<PlotProgressModel>> {
        val currentUserId = userSession.getUserId()
        if (currentUserId == null) return MutableLiveData(emptyList())

        val vgmLiveData = vgmDao.getAllVgmWithUser()
        val plotLiveData = plotRepository.getPlotsWithVgmCount()

        return MediatorLiveData<List<PlotProgressModel>>().apply {
            var rawVgmList: List<VgmWithUser>? = null
            var plotList: List<PlotWithVgmCountModel>? = null

            fun update() {
                val rawVgm = rawVgmList ?: return
                val plots = plotList ?: return

                val vgmList = VgmMapper.mapVgmWithUserToModel(rawVgm)

                val filteredVgm = vgmList.filter {
                    it.idUser == currentUserId &&
                            it.latestTinggiTanaman != null &&
                            it.latestDiameterBatang != null &&
                            it.latestJumlahDaun != null
                }

                val groupedVgm = filteredVgm.groupBy { it.idPlot }

                val result = plots.map { plot ->
                    val jumlahInput = groupedVgm[plot.idPlot]?.size ?: 0
                    PlotProgressModel(
                        idPlot = plot.idPlot,
                        namaPlot = plot.namaPlot,
                        jumlahTarget = plot.jumlahVgm,
                        jumlahInput = jumlahInput
                    )
                }

                value = result
            }

            addSource(vgmLiveData) {
                rawVgmList = it
                update()
            }

            addSource(plotLiveData) {
                plotList = it.data ?: emptyList()
                update()
            }
        }
    }
}

