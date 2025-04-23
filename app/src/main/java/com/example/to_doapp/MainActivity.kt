package com.example.to_doapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.to_doapp.auth.SigninActivity
import com.example.to_doapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user is logged in via SharedPreferences
        val sharedPref = getSharedPreferences("login", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            startActivity(Intent(this, SigninActivity::class.java))
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Logout functionality
        binding.logoutbutton.setOnClickListener {
            sharedPref.edit().clear().apply()
            startActivity(Intent(this, SigninActivity::class.java))
            finish()
        }
    }
}
