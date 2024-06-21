package com.example.urskin.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope

import com.example.urskin.data.pref.UserModel
import com.example.urskin.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository): ViewModel() {
    val isLoading: LiveData<Boolean> = repository.isLoading
    fun getSession(): LiveData<UserModel>{
        return repository.getSession().asLiveData()
    }

    fun logout(){
        viewModelScope.launch{
            repository.logout()
        }
    }
}