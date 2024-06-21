package com.example.urskin.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.urskin.data.pref.UserModel
import com.example.urskin.data.pref.UserPreferences
import com.example.urskin.data.response.LoginResponse
import com.example.urskin.data.response.ProfileResponse
import com.example.urskin.data.response.RegisterResponse
import com.example.urskin.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreferences: UserPreferences,
    private val apiService: ApiService
){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    suspend fun saveSession(user:UserModel){
        userPreferences.saveSession(user)
    }

    fun getSession(): Flow<UserModel>{
        return userPreferences.getSession()
    }

    suspend fun login(email: String,password: String): LoginResponse{
        val response= apiService.login(email, password)
        if (response.status != "fail"){
            val user = response.data?.id?.let { UserModel(email,it, true) }
            if (user != null){
                saveSession(user)
            }
        }
        return response
    }



    suspend fun register(name: String, email: String, password: String): RegisterResponse{
        return apiService.register(name, email, password)

    }


    suspend fun logout(){
        userPreferences.logout()
    }

    suspend fun getProfile(id: String): ProfileResponse{
        return apiService.getProfile(id)
    }
    companion object{
        fun getInstance(
            userPreferences: UserPreferences,
            apiService: ApiService,

        )= UserRepository (userPreferences, apiService)
    }
}