package com.example.mystoryapp1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapp1.data.UserRepository
import com.example.mystoryapp1.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession() =repository.getSession().asLiveData()

    fun story():LiveData<PagingData<ListStoryItem>> = repository.getStoryUser().cachedIn(viewModelScope)
//    fun getStory() = repository.getStoryUser()
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}