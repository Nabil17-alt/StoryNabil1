package com.muhammadnabil.storynabil

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.muhammadnabil.storynabil.animation.applyFadeInAnimations
import com.muhammadnabil.storynabil.api.ApiConfig
import com.muhammadnabil.storynabil.api.LoginCredentials
import com.muhammadnabil.storynabil.api.LoginResponse
import com.muhammadnabil.storynabil.databinding.ActivityLoginBinding
import com.muhammadnabil.storynabil.utils.PreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesHelper = PreferencesHelper(this)

        val buttonViews = listOf(
            binding.edLoginEmail,
            binding.edLoginPassword,
            binding.btnLogin,
            binding.btnOpenRegister
        )
        applyFadeInAnimations(this, buttonViews)

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (!validateInput(email, password)) return@setOnClickListener

            val loginCredentials = LoginCredentials(email, password)
            val call = ApiConfig.getApiService(this).login(loginCredentials)

            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse?.error == false) {
                            preferencesHelper.isLoggedIn = true
                            preferencesHelper.token = loginResponse.loginResult.token

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            showToast(loginResponse?.message ?: "Unknown error")
                        }
                    } else {
                        showToast("Login failed")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    showToast("Error: ${t.message}")
                }
            })
        }

        binding.btnOpenRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            showToast("Email is required")
            return false
        }
        if (password.isEmpty()) {
            showToast("Password is required")
            return false
        }
        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
