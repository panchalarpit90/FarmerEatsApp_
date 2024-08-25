package com.example.farmereats.repository

import com.example.farmereats.network.ApiService
import com.example.farmereats.models.Login
import com.example.farmereats.models.LoginResponse
import com.example.farmereats.models.NewUser
import com.example.farmereats.models.Number
import com.example.farmereats.models.OtpToken
import com.example.farmereats.models.PasswordReset
import com.example.farmereats.utils.ApiResponse
import com.example.farmereats.utils.handleApiResponse
import javax.inject.Inject


class PostRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun login(login: Login): ApiResponse<LoginResponse> {
        return try {
            val response = apiService.login(login)
            when (val data = response.handleApiResponse()) {
                is ApiResponse.Error -> ApiResponse.Error(exception = data.exception)
                is ApiResponse.Success -> ApiResponse.Success(data.data)
            }
        } catch (e: Exception) {
            ApiResponse.Error(Exception("msg error:${e.message}"))
        }

    }

    suspend fun register(newUser: NewUser): ApiResponse<LoginResponse> {
        return try {
            val response = apiService.register(newUser)
            when (val data = response.handleApiResponse()) {
                is ApiResponse.Error -> ApiResponse.Error(exception = data.exception)
                is ApiResponse.Success -> ApiResponse.Success(data.data)
            }
        } catch (e: Exception) {
            ApiResponse.Error(Exception("msg error:${e.message}"))
        }

    }

    suspend fun forgetPassword(getNumber: Number): ApiResponse<LoginResponse> {
        return try {
            val response = apiService.forgetPassword(getNumber)
            when (val data = response.handleApiResponse()) {
                is ApiResponse.Error -> ApiResponse.Error(exception = data.exception)
                is ApiResponse.Success -> ApiResponse.Success(data.data)
            }
        } catch (e: Exception) {
            ApiResponse.Error(Exception("msg error:${e.message}"))
        }

    }

    suspend fun verifyOtp(getOtpToken: OtpToken): ApiResponse<LoginResponse> {
        return try {
            val response = apiService.verifyOtp(getOtpToken)
            when (val data = response.handleApiResponse()) {
                is ApiResponse.Error -> ApiResponse.Error(exception = data.exception)
                is ApiResponse.Success -> ApiResponse.Success(data.data)
            }
        } catch (e: Exception) {
            ApiResponse.Error(Exception("msg error:${e.message}"))
        }

    }

    suspend fun resetPassword(setPasswordReset: PasswordReset): ApiResponse<LoginResponse> {
        return try {
            val response = apiService.resetNewPassword(setPasswordReset)
            when (val data = response.handleApiResponse()) {
                is ApiResponse.Error -> ApiResponse.Error(exception = data.exception)
                is ApiResponse.Success -> ApiResponse.Success(data.data)
            }
        } catch (e: Exception) {
            ApiResponse.Error(Exception("msg error:${e.message}"))
        }

    }
}
