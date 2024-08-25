package com.example.farmereats.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmereats.models.Login
import com.example.farmereats.models.LoginResponse
import com.example.farmereats.repository.PostRepository
import com.example.farmereats.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg
    fun login(login: Login) {
        if (login.email.isEmpty()) {
            _errorMsg.value = "Please Enter Email!"
            return
        }
        if (login.password.isEmpty()) {
            _errorMsg.value = "Please Enter Password!"
            return
        }
        viewModelScope.launch {
            when (val response = repository.login(login)) {
                is ApiResponse.Error -> {
                    _errorMsg.value = response.exception.message
                }

                is ApiResponse.Success -> {
                    _loginResponse.value = response.data
                }

            }

        }
    }

    fun clearErrorMsg() {
        _errorMsg.value = ""
    }

}

