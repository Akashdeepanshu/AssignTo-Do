package com.example.to_doapp.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.to_doapp.MainActivity

import com.example.to_doapp.databinding.ActivitySignupBinding
import com.example.to_doapp.network.ApiClient
import com.example.to_doapp.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private var userImageUri: Uri? = null
    private var userType: String = ""


    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        userImageUri = it
        binding.UserImage.setImageURI(userImageUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.UserImage.setOnClickListener {
            selectImage.launch("image/*")
        }


        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            userType = findViewById<RadioButton>(checkedId).text.toString()
        }


        binding.btnregister.setOnClickListener {
            val name = binding.etNameInput.text.toString()
            val email = binding.etEmailInput.text.toString()
            val password = binding.etPasswordInput.text.toString()
            val confirmPassword = binding.etConfirmPasswordInput.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || userType.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val imageUrl = ""
            val registerRequest = ApiService.RegisterUserRequest(name, email, password, userType, imageUrl)


            registerUser(registerRequest)
        }
    }

    private fun registerUser(registerRequest: ApiService.RegisterUserRequest) {
        val apiService = ApiClient.instance

        apiService.registerUser(registerRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@SignupActivity, "User Registered Successfully", Toast.LENGTH_SHORT).show()


                    val sharedPref = getSharedPreferences("login", MODE_PRIVATE)
                    sharedPref.edit().putBoolean("isLoggedIn", true).apply()


                    startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@SignupActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@SignupActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }


}
