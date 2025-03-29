package com.example.enursery.core.utils

import android.content.Context
import com.example.enursery.R
import com.example.enursery.core.data.source.local.entity.RoleEntity
import com.example.enursery.core.data.source.local.entity.WilayahKerjaEntity
import com.example.enursery.core.data.source.remote.response.PlotResponse
import com.example.enursery.core.data.source.remote.response.UserResponse
import com.example.enursery.core.data.source.remote.response.VgmResponse
import org.json.JSONObject
import java.io.IOException

class JsonHelper(private val context: Context) {

    private fun parsingFileToString(resourceId: Int): String? {
        return try {
            context.resources.openRawResource(resourceId).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun loadUserData(): List<UserResponse> {
        val list = ArrayList<UserResponse>()
        val responseObject = JSONObject(parsingFileToString(R.raw.user).toString())
        val listArray = responseObject.getJSONArray("user")
        for (i in 0 until listArray.length()) {
            val obj = listArray.getJSONObject(i)
            list.add(
                UserResponse(
                    id = obj.getString("id"),
                    nama = obj.getString("nama"),
                    roleId = obj.getString("roleId"),
                    wilayahId = obj.getString("wilayahId"),
                    foto = obj.getString("foto"),
                    email = obj.getString("email"),
                    password = obj.getString("password")
                )
            )
        }
        return list
    }

    fun loadRoleData(): List<RoleEntity> {
        val list = ArrayList<RoleEntity>()
        val responseObject = JSONObject(parsingFileToString(R.raw.role).toString())
        val listArray = responseObject.getJSONArray("roles")
        for (i in 0 until listArray.length()) {
            val obj = listArray.getJSONObject(i)
            list.add(
                RoleEntity(
                    id = obj.getString("id"),
                    name = obj.getString("name")
                )
            )
        }
        return list
    }

    fun loadWilayahData(): List<WilayahKerjaEntity> {
        val list = ArrayList<WilayahKerjaEntity>()
        val responseObject = JSONObject(parsingFileToString(R.raw.wilayah_kerja).toString())
        val listArray = responseObject.getJSONArray("wilayah")
        for (i in 0 until listArray.length()) {
            val obj = listArray.getJSONObject(i)
            list.add(
                WilayahKerjaEntity(
                    id = obj.getString("id"),
                    name = obj.getString("name")
                )
            )
        }
        return list
    }

    fun loadPlotData(): List<PlotResponse> {
        val list = ArrayList<PlotResponse>()
        val responseObject = JSONObject(parsingFileToString(R.raw.plot).toString())
        val listArray = responseObject.getJSONArray("plot")

        for (i in 0 until listArray.length()) {
            val obj = listArray.getJSONObject(i)

            val plot = PlotResponse(
                idPlot = obj.getString("idPlot"),
                namaPlot = obj.getString("namaPlot"),
                luasArea = obj.getDouble("luasArea"),
                tanggalTanam = obj.getString("tanggalTanam"),           // masih String di sini
                tanggalTransplantasi = obj.getString("tanggalTransplantasi"), // masih String juga
                varietas = obj.getString("varietas"),
                latitude = obj.getDouble("latitude"),
                longitude = obj.getDouble("longitude"),
                jumlahBibit = obj.getInt("jumlahBibit")
            )

            list.add(plot)
        }

        return list
    }

    fun loadVgmData(): List<VgmResponse> {
        val list = ArrayList<VgmResponse>()
        val responseObject = JSONObject(parsingFileToString(R.raw.vgm).toString())
        val listArray = responseObject.getJSONArray("vgm")

        for (i in 0 until listArray.length()) {
            val obj = listArray.getJSONObject(i)

            val vgm = VgmResponse(
                idBibit = obj.getString("idBibit"),
                idPlot = obj.getString("idPlot"),
                idPekerja = obj.getString("idPekerja"),
                status = obj.getString("status"),           // masih String di sini
                latestTinggiTanaman = obj.getDouble("latestTinggiTanaman"),
                latestDiameterBatang = obj.getDouble("latestDiameterBatang"),
                latestJumlahDaun = obj.getInt("latestJumlahDaun"),
                latestTanggalInput = obj.getString("latestTanggalInput"), // masih String juga
                latestFoto = obj.getString("latestFoto")
            )

            list.add(vgm)
        }

        return list
    }
}
