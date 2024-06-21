package com.example.urskin.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.urskin.data.pref.UserModel
import com.example.urskin.data.repository.UserRepository
import com.example.urskin.data.response.ErrorResponse
import com.example.urskin.data.response.LoginErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository): ViewModel() {
    private val _data = MutableLiveData<Data>()
    val data: LiveData<Data>
        get()= _data

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String){
        _isLoading.value = true
        viewModelScope.launch {
            try{
                val response = repository.login(email, password)
                if (response.status != "false"){
                    val user = response.data!!.id?.let { UserModel(email,it,true) }
                    if(user != null){
                        repository.saveSession(user)
                    }
                    _data.value = user?.let { Data.Success(it) }
                }else {
                    val errorResponse = LoginErrorResponse(status = "fail", message = response.message ?: "Unknown error")
                    _data.value = Data.Failed(errorResponse)
                }
            }catch (e: HttpException){
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginErrorResponse::class.java)
                val errorMessage = errorBody.message
                val errorStatus = errorBody.status
                val errorResponse = LoginErrorResponse(status = errorStatus, message = errorMessage)
                _data.value = Data.Failed(errorResponse)
            }finally {
                _isLoading.value=false
            }
        }
    }

    sealed class Data{
        data class Success(val user:UserModel): Data()
        data class Failed(val errorResponse: LoginErrorResponse): Data()
    }
}

