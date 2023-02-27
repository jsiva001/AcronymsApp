package com.example.myapplication.model.api

import com.example.myapplication.model.data.AcronymsDataItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AcronymsDataAPI {

    @GET("software/acromine/dictionary.py")
    suspend fun getAcronymsData(@Query("sf") sf: String): Response<ArrayList<AcronymsDataItem>>
}