package com.example.mystoryapp1.viewmodel


import androidx.lifecycle.ViewModel

import com.example.mystoryapp1.data.UserRepository

class RegisterViewModel(private  val userRepository: UserRepository): ViewModel() {

    fun register(name:String,email:String,password:String) = userRepository.userRegistrasi(name, email, password)


}