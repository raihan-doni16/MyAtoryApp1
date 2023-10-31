package com.example.mystoryapp1.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mystoryapp1.data.UserRepository

class LocationViewModel(private  val repository: UserRepository):ViewModel() {
    fun getLocationUser() = repository.getLocation()

}