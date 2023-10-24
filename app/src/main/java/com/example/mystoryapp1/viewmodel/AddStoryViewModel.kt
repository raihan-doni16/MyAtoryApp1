package com.example.mystoryapp1.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mystoryapp1.data.UserRepository
import java.io.File

class AddStoryViewModel (private  val userRepository: UserRepository): ViewModel(){
    fun postImage(photoFile: File, description: String) = userRepository.postStoryUser(photoFile, description)
}