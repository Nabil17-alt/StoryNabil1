package com.muhammadnabil.storynabil.utils

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.muhammadnabil.storynabil.api.DetailStoryResponse
import com.muhammadnabil.storynabil.api.FileUploadResponse
import com.muhammadnabil.storynabil.database.StoryDatabase
import com.muhammadnabil.storynabil.api.StoryAPI
import com.muhammadnabil.storynabil.model.Story
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: StoryAPI, private val token: String) {

    fun getStory(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, "Bearer $token")
            }
        ).liveData

    }

    fun getStoryDetail(storyId: String): Call<DetailStoryResponse> {
        return apiService.getStoryDetail(storyId, "Bearer $token")
    }

    fun uploadImage(file: MultipartBody.Part, description: RequestBody): Call<FileUploadResponse> {
        return apiService.uploadImage(file, description, "Bearer $token")
    }
}