package com.example.to_doapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.to_doapp.auth.SigninActivity
import com.example.to_doapp.databinding.ActivityEmployeeMainBinding
import com.example.to_doapp.network.ApiClient
import com.example.to_doapp.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmployeeMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeMainBinding
    private lateinit var employeeWorkAdapter: EmployeeActivityAdapter

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


        employeeWorkAdapter = EmployeeActivityAdapter { taskId, newStatus ->
            updateTaskStatus(taskId, newStatus) // Handle status change
        }

        binding.rvWork.apply {
            layoutManager = LinearLayoutManager(this@EmployeeMainActivity)
            adapter = employeeWorkAdapter
        }

        showTask()
    }

    private fun showTask() {
        val sharedPref = getSharedPreferences("login", MODE_PRIVATE)
        val userId = sharedPref.getString("userId", null)

        if (userId != null) {
            ApiClient.instance.getTasks(userId).enqueue(object : Callback<List<ApiService.Taskinfo>> {
                override fun onResponse(call: Call<List<ApiService.Taskinfo>>, response: Response<List<ApiService.Taskinfo>>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            employeeWorkAdapter.differ.submitList(it)
                        }
                    } else {
                        Toast.makeText(this@EmployeeMainActivity, "Failed to load tasks", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<ApiService.Taskinfo>>, t: Throwable) {
                    Toast.makeText(this@EmployeeMainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTaskStatus(taskId: String, newStatus: String) {
        val statusBody = mapOf("workStatus" to newStatus)

        ApiClient.instance.updateTaskStatus(taskId, statusBody).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EmployeeMainActivity, "Task status updated", Toast.LENGTH_SHORT).show()
                    showTask()
                } else {
                    Toast.makeText(this@EmployeeMainActivity, "Failed to update task status", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@EmployeeMainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { _, _ ->
            val sharedPref = getSharedPreferences("login", MODE_PRIVATE)
            sharedPref.edit().putBoolean("isLoggedIn", false).apply()
            startActivity(Intent(this, SigninActivity::class.java))
            finish()
        }
        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        builder.setCancelable(false)
        builder.show()
    }
}
