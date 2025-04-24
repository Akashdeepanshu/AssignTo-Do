package com.example.to_doapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.to_doapp.databinding.ActivityForgetPasswordBinding
import com.example.to_doapp.network.ApiClient
import com.example.to_doapp.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // Back to Signin click
        binding.textView4.setOnClickListener {
            startActivity(Intent(this, SigninActivity::class.java))
        }

        // Submit Reset Password
        binding.forgetpassbutton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()

            if (email.isEmpty()) {
                binding.emailInput.error = "Enter your email"
                return@setOnClickListener
            }

            val resetRequest = ApiService.ForgotPasswordRequest(email)

            ApiClient.instance.forgotPassword(resetRequest).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ForgetPasswordActivity, "Reset email sent!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ForgetPasswordActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@ForgetPasswordActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
