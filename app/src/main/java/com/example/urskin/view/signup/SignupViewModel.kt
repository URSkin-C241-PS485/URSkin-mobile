package com.example.urskin.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.urskin.data.repository.UserRepository
import com.example.urskin.data.response.ErrorResponse
import com.example.urskin.data.response.RegisterResponse
import com.example.urskin.data.response.SignupErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupViewModel(private val repository: UserRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    fun register(name: String, email: String, password: String): LiveData<RegisterResponse> {
        val resultLiveData = MutableLiveData<RegisterResponse>()
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.register(name, email, password)
                resultLiveData.postValue(result)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, SignupErrorResponse::class.java)
                val errorMessage = errorBody.message
                val errorStatus = errorBody.status
                resultLiveData.postValue(RegisterResponse(error = errorStatus, message = errorMessage))
            } finally {
                _isLoading.value = false
            }
        }
        return resultLiveData
    }
}