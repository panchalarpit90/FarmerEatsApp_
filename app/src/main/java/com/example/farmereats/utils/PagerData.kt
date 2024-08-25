package com.example.farmereats.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PagerData(
    val image:Int,
    val title:Int,
    val desc:Int,
    val color:Int,
    val dotImage:Int

):Parcelable
