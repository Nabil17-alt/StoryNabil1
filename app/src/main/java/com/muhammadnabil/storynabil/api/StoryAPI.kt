package com.muhammadnabil.storynabil.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface StoryAPI {
    @POST("register")
    fun register(@Body user: RegisterCredentials): Call<RegisterResponse>

    @POST("login")
    fun login(@Body credentials: LoginCredentials): Call<LoginResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): GetStoriesResponse

    @GET("stories/{id}")
    fun getStoryDetail(
        @Path("id") storyId: String,
        @Header("Authorization") token: String
    ): Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<FileUploadResponse>

    @GET("stories")
    fun getLocationStories(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 1
    ): Call<StoriesResponse>
}