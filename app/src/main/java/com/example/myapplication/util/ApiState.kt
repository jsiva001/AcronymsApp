package com.example.myapplication.util

sealed class ApiState {
    object Loading : ApiState()
    class Failure(val msg: Throwable) : ApiState()
    class Exception(val code: Int, val msg: String) : ApiState()
    class Success(val data: Any?) : ApiState()
    object Empty : ApiState()
}