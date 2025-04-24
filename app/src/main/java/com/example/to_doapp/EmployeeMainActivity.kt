package com.example.to_doapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.to_doapp.auth.SigninActivity
import com.example.to_doapp.databinding.ActivityEmployeeMainBinding
import com.example.to_doapp.databinding.ActivityMainBinding

class EmployeeMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmployeeMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val sharedPref = getSharedPreferences("login", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            startActivity(Intent(this, SigninActivity::class.java))
            finish()
            return
        }

        binding = ActivityEmployeeMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tbEmployee.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout -> {
                    showLogoutDialog()
                    true
                }
                else -> false
            }
        }
    }
    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        val alertDialog = builder.create()
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { _, _ ->
            val sharedPref =
                this.getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
            sharedPref.edit().putBoolean("isLoggedIn", false).apply()
            startActivity(Intent(this, SigninActivity::class.java))
            this.finish()
        }
        builder.setNegativeButton("No") { _, _ ->
            alertDialog.dismiss()
        }
            .show()
            .setCancelable(false)
    }



}
