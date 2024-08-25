package com.example.farmereats.models

data class PasswordReset(
    val cpassword: String,
    val password: String,
    val token: String
)