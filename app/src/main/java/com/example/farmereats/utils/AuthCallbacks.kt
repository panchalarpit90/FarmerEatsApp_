package com.example.farmereats.utils

interface AuthCallbacks {
    fun onGoogleSignInSuccess()
    fun onFacebookSignInSuccess()
    fun onAuthFailure(errorMessage: String)
}