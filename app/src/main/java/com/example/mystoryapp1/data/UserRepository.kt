package com.example.mystoryapp1.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.mystoryapp1.data.pref.UserModel
import com.example.mystoryapp1.data.pref.UserPreference
import com.example.mystoryapp1.data.response.AddResponse
import com.example.mystoryapp1.data.response.ErrorResponse
import com.example.mystoryapp1.data.response.ListStoryItem
import com.example.mystoryapp1.data.response.LoginResponse
import com.example.mystoryapp1.data.response.RegisterResponse
import com.example.mystoryapp1.remote.ApiConfig
import com.example.mystoryapp1.remote.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.net.SocketTimeoutException

class UserRepository private constructor( private val apiService: ApiService,private val userPreference: UserPreference ) {

    fun userRegistrasi(name:String,email:String, password:String):LiveData<Result<RegisterResponse>> = liveData{
      emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            if (response.error == false) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message ?: "An error occurred"))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody?.message ?: "An error occurred"
            emit(Result.Error("Registration failed: $errorMessage"))
        }catch (e: Exception){
            emit(Result.Error("Internet Issues"))
        }
    }

    fun userLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.loginUser(email, password)
            if (response.error == false){
                val aut = UserModel(
                    name = response.loginResult.name ?: "",
                    userId = response.loginResult.userId ?:"",
                    token = response.loginResult.token,
                    isLogin = true
                )
                ApiConfig.token = response.loginResult.token
                userPreference.saveSession(aut)
                emit(Result.Success(response))
            }else{
                emit(Result.Error(response.message ?: "Error"))
            }
        }catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody?.message ?: "An error occurred"
            emit(Result.Error("Login failed: $errorMessage"))
        }catch (e: Exception){
            emit(Result.Error("Internet Issues"))
        }


    }
    fun postStoryUser(photoFile: File, description: String) = liveData {
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestPhotoFile = photoFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            photoFile.name,
            requestPhotoFile
        )
        try {
            val userPref = runBlocking { userPreference.getSession().first() }
            val response = ApiConfig.getApiService(userPref.token)
            val successResponse = response.postStory(multipartBody,requestBody)
            emit(Result.Success(successResponse))
        }catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddResponse::class.java)
            emit(errorResponse?.message?.let { Result.Error(it) })
        }
    }
    fun getStoryUser(): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val userPref = runBlocking { userPreference.getSession().first() }
            val response = ApiConfig.getApiService(userPref.token)
            val storyResponse = response.getStories()
            val story = storyResponse.listStory
            val storyList = story.map { data ->
                ListStoryItem(
                    data?.photoUrl,
                    data?.createdAt,
                    data?.name,
                    data?.description,
                    data?.lat,
                    data?.id,
                    data?.lon,

                    )
            }

            if (storyResponse.error == false){
                emit(Result.Success(storyList))
            }else{
                emit(Result.Error(storyResponse.message ?: "Error"))
            }
        }catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody?.message ?: "An error occurred"
            emit(Result.Error("Failed: $errorMessage"))
        }catch (e: Exception){
            emit(Result.Error("Internet Issues"))
        }catch (e: SocketTimeoutException){
            emit(Result.Error("Read timeout occurred"))
        }
    }
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService, userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService,userPreference)
            }.also { instance = it }
    }
}