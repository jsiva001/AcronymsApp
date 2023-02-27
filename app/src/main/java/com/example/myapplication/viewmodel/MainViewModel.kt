package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.data.AcronymsDataItem
import com.example.myapplication.model.repository.AcronymsDataRepo
import com.example.myapplication.util.NetworkHelper
import com.example.myapplication.util.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val acronymsDataRepo: AcronymsDataRepo,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val acronymsLiveData = MutableLiveData<ApiState>()
    val acronymsData: LiveData<ApiState> get() = acronymsLiveData

    fun fetchAcronymsData(sf: String) {
        if (networkHelper.isNetworkConnected()) {
            viewModelScope.launch {
                acronymsLiveData.postValue(ApiState.Loading)
                if (networkHelper.isNetworkConnected()) {
                    try {
                        val response =
                            withContext(Dispatchers.IO) { acronymsDataRepo.getAcronymsData(sf) }
                        val postValue = when {
                            response.isSuccessful && response.body() != null -> {
                                val resData = response.body() as ArrayList<*>
                                val acronymsDataItem = (resData[0] as AcronymsDataItem)
                                ApiState.Success(acronymsDataItem)
                            }
                            response.isSuccessful && response.body()?.isEmpty()!! -> {
                                ApiState.Empty
                            }
                            else -> {
                                ApiState.Exception(response.code(), response.message())
                            }
                        }
                        acronymsLiveData.postValue(postValue)
                    } catch (e: Exception) {
                        ApiState.Failure(e)
                    }
                }
            }
        } else {
            acronymsLiveData.postValue(
                ApiState.Exception(
                    503,
                    "No internet connection"
                )
            )
        }
    }
}