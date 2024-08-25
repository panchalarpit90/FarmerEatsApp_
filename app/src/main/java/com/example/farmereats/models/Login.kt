package com.example.farmereats.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Login(
    val device_token: String,
    val email: String,
    val password: String,
    val role: String,
    val social_id: String,
    val type: String
):Parcelable