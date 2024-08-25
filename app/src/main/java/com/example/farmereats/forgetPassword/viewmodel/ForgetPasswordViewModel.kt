package com.example.farmereats.forgetPassword.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmereats.models.LoginResponse
import com.example.farmereats.models.Number
import com.example.farmereats.repository.PostRepository
import com.example.farmereats.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(private val repository: PostRepository) :
    ViewModel() {

    private val _getResponse = MutableLiveData<LoginResponse>()
    val getResponse: LiveData<LoginResponse> = _getResponse

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg
    fun forgetPasswordFun(number: Number) {
        if (number.mobile.isEmpty()) {
            _errorMsg.value = "Please Enter Phone Number!"
            return
        }
        viewModelScope.launch {
            when (val response = repository.forgetPassword(number)) {
                is ApiResponse.Error -> {
                    _errorMsg.value = response.exception.message
                }

                is ApiResponse.Success -> {
                    _getResponse.value = response.data
                }

            }

        }
    }

    fun clearErrorMsg() {
        _errorMsg.value = ""
    }

}
