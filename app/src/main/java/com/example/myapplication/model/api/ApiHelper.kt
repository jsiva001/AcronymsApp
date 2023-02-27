package com.example.myapplication.model.api

import com.example.myapplication.model.data.AcronymsDataItem
import retrofit2.Response
import javax.inject.Inject

class ApiHelper @Inject constructor(private val api: AcronymsDataAPI) {
    suspend fun getAcronymsData(sf: String): Response<ArrayList<AcronymsDataItem>> =
        api.getAcronymsData(sf)
}