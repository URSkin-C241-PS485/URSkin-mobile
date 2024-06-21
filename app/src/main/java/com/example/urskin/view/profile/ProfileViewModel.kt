package com.example.urskin.view.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.urskin.data.repository.UserRepository
import com.example.urskin.data.response.ProfileResponse
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository): ViewModel() {

    val profileData: MutableLiveData<ProfileResponse> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()



}