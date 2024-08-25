package com.example.farmereats.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BusinessHours(
    val mon: List<String> = emptyList(),
    val tue: List<String> = emptyList(),
    val wed: List<String> = emptyList(),
    val thu: List<String> = emptyList(),
    val fri: List<String> = emptyList(),
    val sat: List<String> = emptyList(),
    val sun: List<String> = emptyList()
):Parcelable
