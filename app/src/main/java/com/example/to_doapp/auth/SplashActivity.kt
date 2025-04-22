package com.example.to_doapp.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.to_doapp.MainActivity
import com.example.to_doapp.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Handler(Looper.getMainLooper()).postDelayed({
            // Start the MainActivity after the delay
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)
    }
}
