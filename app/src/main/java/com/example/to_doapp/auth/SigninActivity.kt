package com.example.to_doapp.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.to_doapp.BossMainActivity
import com.example.to_doapp.EmployeeMainActivity
import com.example.to_doapp.databinding.ActivitySigninBinding
import com.example.to_doapp.network.ApiClient
import com.example.to_doapp.network.ApiService

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginbutton.setOnClickListener {
            val email = binding.etemailInput.text.toString().trim()
            val password = binding.etpasswordInput.text.toString().trim()

            if (!isValidInput(email, password)) return@setOnClickListener

            val loginRequest = ApiService.LoginRequest(email, password)
            loginUser(loginRequest)
        }

        binding.signupbtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.forgetpassbtn.setOnClickListener{
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }


    }

    private fun loginUser(loginRequest: ApiService.LoginRequest) {
        val apiService = ApiClient.instance

        apiService.loginUser(loginRequest).enqueue(object : Callback<ApiService.LoginResponse> {
            override fun onResponse(call: Call<ApiService.LoginResponse>, response: Response<ApiService.LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!

                    Toast.makeText(this@SigninActivity, "Login successful!", Toast.LENGTH_SHORT).show()

                    val sharedPref = getSharedPreferences("login", MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putBoolean("isLoggedIn", true)
                        putString("userType", loginResponse.user_type)
                        putString("userId", loginResponse.user_id)
                        putString("userName", loginResponse.name)
                        apply()
                    }

                    val intent = when (loginResponse.user_type) {
                        "Boss" -> Intent(this@SigninActivity, BossMainActivity::class.java)
                        "Employee" -> Intent(this@SigninActivity, EmployeeMainActivity::class.java)
                        else -> null
                    }

                    if (intent != null) {
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@SigninActivity, "Unknown user type", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this@SigninActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiService.LoginResponse>, t: Throwable) {
                Toast.makeText(this@SigninActivity, "Login failed: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun isValidInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.etemail.error = "Email is required"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etemail.error = "Invalid email"
            return false
        }
        if (password.isEmpty()) {
            binding.etpassword.error = "Password is required"
            return false
        }
        if (password.length < 6) {
            binding.etpassword.error = "Password must be at least 6 characters"
            return false
        }
        return true
    }
}
