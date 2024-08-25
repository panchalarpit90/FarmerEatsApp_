package com.example.farmereats.utils

import com.example.farmereats.models.ApiErrorResponse
import com.google.gson.Gson
import retrofit2.Response

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val exception: Throwable) : ApiResponse<Nothing>()
}

private fun parseError(errorBody: String): ApiErrorResponse {
    return Gson().fromJson(errorBody, ApiErrorResponse::class.java)
}

fun <T> Response<T>.handleApiResponse(): ApiResponse<T> {
    return if (this.isSuccessful) {
        val body = this.body()
        if (body != null) {
            ApiResponse.Success(body)
        } else {
            ApiResponse.Error(Exception("Unexpected empty response body"))
        }
    } else {
        val errorBody = this.errorBody()?.string()
        if (errorBody != null) {
            val apiError = parseError(errorBody)
            ApiResponse.Error(Exception(apiError.message))
        } else {
            ApiResponse.Error(Exception("Unknown API error"))
        }
    }
}