package com.example.farmereats.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(
    val message: String,
    val success: String,
    val token:String
):Parcelable