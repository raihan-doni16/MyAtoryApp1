package com.example.mystoryapp1.data

import android.content.Context
import com.example.mystoryapp1.data.pref.UserPreference
import com.example.mystoryapp1.data.pref.dataStore
import com.example.mystoryapp1.remote.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {

        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)

        return UserRepository.getInstance( apiService,pref)
    }
}