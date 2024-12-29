package com.muhammadnabil.storynabil.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import okhttp3.OkHttpClient

class PreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)

    var isLoggedIn: Boolean
        get() = sharedPreferences.getBoolean("isLoggedIn", false)
        set(value) = sharedPreferences.edit().putBoolean("isLoggedIn", value).apply()

    var token: String?
        get() {
            val savedToken = sharedPreferences.getString("token", null)
            Log.d("PreferencesHelper", "Get token: $savedToken")
            return savedToken?.let {
                if (it.startsWith("Bearer ")) it.substring(7) else it
            }
        }
        set(value) {
            val tokenToSave = value?.let {
                if (it.startsWith("Bearer ")) it else "Bearer $it"
            }
            if (tokenToSave != null && tokenToSave.length > 7) {
                sharedPreferences.edit().putString("token", tokenToSave).apply()
                Log.d("PreferencesHelper", "Tokens are stored: $tokenToSave")
            } else {
                Log.e("PreferencesHelper", "Invalid or empty token")
            }
        }

    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val tokenValue = token
            val requestBuilder = chain.request().newBuilder()
            if (!tokenValue.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $tokenValue")
            }
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()

    fun clear() {
        sharedPreferences.edit().clear().apply()
        Log.d("PreferencesHelper", "Preferences cleared")
    }

    fun handleExpiredToken() {
        clear()
        Log.d("PreferencesHelper", "Token expired, preferences data deleted")
    }
}