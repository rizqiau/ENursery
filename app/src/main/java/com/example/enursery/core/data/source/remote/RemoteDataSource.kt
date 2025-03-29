package com.example.enursery.core.data.source.remote

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.enursery.core.data.source.local.entity.RoleEntity
import com.example.enursery.core.data.source.local.entity.WilayahKerjaEntity
import com.example.enursery.core.data.source.remote.network.ApiResponse
import com.example.enursery.core.data.source.remote.response.PlotResponse
import com.example.enursery.core.data.source.remote.response.UserResponse
import com.example.enursery.core.data.source.remote.response.VgmResponse
import com.example.enursery.core.utils.JsonHelper
import org.json.JSONException

class RemoteDataSource private constructor(private val jsonHelper: JsonHelper) {
    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(helper: JsonHelper): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(helper)
            }
    }

    fun getUserData(): LiveData<ApiResponse<List<UserResponse>>> {
        val resultData = MutableLiveData<ApiResponse<List<UserResponse>>>()

        // Simulasi delay untuk pengambilan data
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            try {
                val dataArray = jsonHelper.loadUserData()
                if (dataArray.isNotEmpty()) {
                    resultData.value = ApiResponse.Success(dataArray)
                } else {
                    resultData.value = ApiResponse.Empty
                }
            } catch (e: JSONException) {
                resultData.value = ApiResponse.Error(e.toString())
                Log.e("RemoteDataSource", e.toString())
            }
        }, 2000)

        return resultData
    }

    fun getRoleData(): List<RoleEntity> {
        return jsonHelper.loadRoleData()
    }

    fun getWilayahData(): List<WilayahKerjaEntity> {
        return jsonHelper.loadWilayahData()
    }

    fun getPlotData(): LiveData<ApiResponse<List<PlotResponse>>> {
        val result = MutableLiveData<ApiResponse<List<PlotResponse>>>()

        Handler(Looper.getMainLooper()).postDelayed({
            try {
                val data = jsonHelper.loadPlotData()
                if (data.isNotEmpty()) {
                    result.value = ApiResponse.Success(data)
                } else {
                    result.value = ApiResponse.Empty
                }
            } catch (e: Exception) {
                result.value = ApiResponse.Error(e.message ?: "Error parsing plot.json")
            }
        }, 1000)

        return result
    }

    fun getVgmData(): LiveData<ApiResponse<List<VgmResponse>>> {
        val result = MutableLiveData<ApiResponse<List<VgmResponse>>>()

        Handler(Looper.getMainLooper()).postDelayed({
            try {
                val data = jsonHelper.loadVgmData()
                if (data.isNotEmpty()) {
                    result.value = ApiResponse.Success(data)
                    Log.d("RemoteDataSource", "getVgmData: $data")
                } else {
                    result.value = ApiResponse.Empty
                }
            } catch (e: Exception) {
                result.value = ApiResponse.Error(e.message ?: "Error parsing plot.json")
            }
        }, 1000)

        return result
    }
}