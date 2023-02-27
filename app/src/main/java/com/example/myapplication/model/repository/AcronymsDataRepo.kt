package com.example.myapplication.model.repository

import com.example.myapplication.model.api.ApiHelper
import javax.inject.Inject

class AcronymsDataRepo @Inject constructor(private val apiHelper: ApiHelper) {
    suspend fun getAcronymsData(sf: String) = apiHelper.getAcronymsData(sf)
}