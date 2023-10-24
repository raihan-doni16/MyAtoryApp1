package com.example.mystoryapp1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp1.data.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession() =repository.getSession().asLiveData()

    fun getStory() = repository.getStoryUser()

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}