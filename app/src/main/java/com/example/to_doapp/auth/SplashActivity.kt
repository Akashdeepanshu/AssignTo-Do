package com.example.to_doapp.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.to_doapp.BossMainActivity
import com.example.to_doapp.EmployeeMainActivity
import com.example.to_doapp.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPref = getSharedPreferences("login", MODE_PRIVATE)
            val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
            val userType = sharedPref.getString("userType", "")

            if (isLoggedIn) {
                val intent = when (userType) {
                    "Boss" -> Intent(this, BossMainActivity::class.java)
                    "Employee" -> Intent(this, EmployeeMainActivity::class.java)
                    else -> Intent(this, SigninActivity::class.java)
                }
                startActivity(intent)
            } else {
                startActivity(Intent(this, SigninActivity::class.java))
            }

            finish()
        }, 3000)
    }
}
