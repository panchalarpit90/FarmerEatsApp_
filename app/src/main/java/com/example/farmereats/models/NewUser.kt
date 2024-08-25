package com.example.farmereats.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewUser(
    val address: String? = null,
    val business_hours: BusinessHours? = null,
    val business_name: String? = null,
    val city: String? = null,
    val device_token: String? = null,
    val email: String? = null,
    val full_name: String? = null,
    val informal_name: String? = null,
    val password: String? = null,
    val phone: String? = null,
    val registration_proof: String? = null,
    val role: String? = null,
    val social_id: String? = null,
    val state: String? = null,
    val type: String? = null,
    val zip_code: Int? = null
):Parcelable