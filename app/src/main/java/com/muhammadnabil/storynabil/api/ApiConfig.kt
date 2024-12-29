package com.muhammadnabil.storynabil.api

import android.content.Context
import com.muhammadnabil.storynabil.utils.PreferencesHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig(private val context: Context) {
    companion object {
        fun getApiService(context: Context): StoryAPI {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val authInterceptor = Interceptor { chain ->
                val originalRequest = chain.request()

                val preferencesHelper = PreferencesHelper(context)
                val token = preferencesHelper.token

                val newRequest = originalRequest.newBuilder()
                    .apply {
                        token?.let {
                            header("Authorization", "Bearer $it")
                        }
                    }
                    .build()

                chain.proceed(newRequest)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://story-api.dicoding.dev/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(StoryAPI::class.java)
        }
    }
}