package com.muhammadnabil.storynabil

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.muhammadnabil.storynabil.animation.applyFadeInAnimations
import com.muhammadnabil.storynabil.api.ApiConfig
import com.muhammadnabil.storynabil.api.RegisterCredentials
import com.muhammadnabil.storynabil.api.RegisterResponse
import com.muhammadnabil.storynabil.databinding.ActivityRegisterBinding
import com.muhammadnabil.storynabil.utils.MyEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var myEditText: MyEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buttonViews = listOf(
            binding.edRegisterName,
            binding.edRegisterEmail,
            binding.edRegisterPassword,
            binding.btnRegister
        )
        applyFadeInAnimations(this, buttonViews)

        myEditText = binding.edRegisterPassword
        myEditText.listener = object : MyEditText.OnPasswordLengthChanged {
            override fun onLengthChanged(length: Int) {
                binding.btnRegister.text = if (length >= 8) "Create Account" else "Password is too short"
                setMyButtonEnable()
            }
        }

        setMyButtonEnable()

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (!validateInput(name, email, password)) return@setOnClickListener

            val user = RegisterCredentials(name, email, password)
            val call = ApiConfig.getApiService(this).register(user)

            call.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        if (registerResponse?.error == false) {
                            navigateToLogin()
                        } else {
                            showToast(registerResponse?.message ?: "Unknown error")
                        }
                    } else {
                        showToast("Registration failed")
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    showToast("Error: ${t.message}")
                }
            })
        }

        binding.tvLogin.setOnClickListener {
            navigateToLogin()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        when {
            name.isEmpty() -> {
                showToast("Name is required")
                return false
            }
            email.isEmpty() -> {
                showToast("Email is required")
                return false
            }
            password.isEmpty() -> {
                showToast("Password is required")
                return false
            }
            password.length < 8 -> {
                showToast("Password must be at least 8 characters")
                return false
            }
        }
        return true
    }

    private fun setMyButtonEnable() {
        val result = myEditText.text
        binding.btnRegister.isEnabled = result != null && result.toString().isNotEmpty() && result.length >= 8
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
