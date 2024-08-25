package com.example.farmereats.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmereats.models.NewUser
import com.example.farmereats.models.LoginResponse
import com.example.farmereats.repository.PostRepository
import com.example.farmereats.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private val _newUser = MutableLiveData<NewUser>(NewUser())
    val newUser: LiveData<NewUser> get() = _newUser

    private val _registrationResponse = MutableLiveData<ApiResponse<LoginResponse>>()
    val registrationResponse: LiveData<ApiResponse<LoginResponse>> get() = _registrationResponse

    fun updateUser(user: NewUser) {
        _newUser.value = user
    }

    fun registerUser() {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.register(_newUser.value ?: NewUser())
                _registrationResponse.postValue(result)
            } catch (e: Exception) {
                _registrationResponse.postValue(ApiResponse.Error(Exception("Unknown error: ${e.message}")))

            }
        }
    }

}

