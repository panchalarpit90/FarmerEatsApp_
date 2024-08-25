package com.example.farmereats.network

import com.example.farmereats.models.Login
import com.example.farmereats.models.LoginResponse
import com.example.farmereats.models.NewUser
import com.example.farmereats.models.Number
import com.example.farmereats.models.OtpToken
import com.example.farmereats.models.PasswordReset
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Body


interface ApiService {
    @POST("user/login")
    suspend fun login(@Body request: Login): Response<LoginResponse>

    @POST("user/register")
    suspend fun register(@Body request: NewUser): Response<LoginResponse>

    @POST("user/forgot-password")
    suspend fun forgetPassword(@Body request: Number): Response<LoginResponse>

    @POST("user/verify-otp")
    suspend fun verifyOtp(@Body request: OtpToken): Response<LoginResponse>

    @POST("user/reset-password")
    suspend fun resetNewPassword(@Body request: PasswordReset): Response<LoginResponse>

}
