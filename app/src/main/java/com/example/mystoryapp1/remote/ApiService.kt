package com.example.mystoryapp1.remote

import com.example.mystoryapp1.data.response.AddResponse
import com.example.mystoryapp1.data.response.DetailResponse
import com.example.mystoryapp1.data.response.ListStoryItem
import com.example.mystoryapp1.data.response.LoginResponse
import com.example.mystoryapp1.data.response.RegisterResponse
import com.example.mystoryapp1.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password")password: String
    ): LoginResponse

//    @GET("stories")
//    suspend fun getStories(
//        @Query("page") page: Int = 1,
//        @Query("size") size: Int = 20
//    ):Call< StoryResponse>
    @GET("stories")
    suspend fun getstory(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): StoryResponse
    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location : Int = 1,
    ): StoryResponse
    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): AddResponse


}