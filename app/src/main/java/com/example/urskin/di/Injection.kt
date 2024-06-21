package com.example.urskin.di

import android.content.Context
import com.example.urskin.data.pref.UserPreferences
import com.example.urskin.data.pref.dataStore
import com.example.urskin.data.repository.UserRepository
import com.example.urskin.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository{
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService()

        return UserRepository.getInstance(pref,apiService)
    }
}