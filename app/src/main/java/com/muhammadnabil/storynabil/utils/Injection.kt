package com.muhammadnabil.storynabil.utils

import android.content.Context
import com.muhammadnabil.storynabil.api.ApiConfig
import com.muhammadnabil.storynabil.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService(context)
        val preferencesHelper = PreferencesHelper(context)
        val token = "Bearer ${preferencesHelper.token}"
        return StoryRepository(database, apiService, token)
    }
}