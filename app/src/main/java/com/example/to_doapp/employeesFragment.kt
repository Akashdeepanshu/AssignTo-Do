package com.example.to_doapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.to_doapp.auth.SigninActivity
import com.example.to_doapp.databinding.FragmentEmployeesBinding
import com.example.to_doapp.network.ApiClient
import com.example.to_doapp.network.ApiService


class employeesFragment : Fragment() {

    private lateinit var binding: FragmentEmployeesBinding
    private lateinit var employeeAdapter: EmployeesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEmployeesBinding.inflate(inflater, container, false)


        binding.apply {
            tbEmployee.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.logout -> {
                        showLogoutDialog()
                        true
                    }
                    else -> false
                }
            }
        }
        prepareRVForEmployeesAdapter()
        showAllEmployeeData()


        return binding.root
    }

    private fun showAllEmployeeData() {
        val apiService = ApiClient.instance
        apiService.getAllEmployeeData().enqueue(object : retrofit2.Callback<List<ApiService.Employeeinfo>> {
            override fun onResponse(
                call: retrofit2.Call<List<ApiService.Employeeinfo>>,
                response: retrofit2.Response<List<ApiService.Employeeinfo>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val employeeList = response.body()
                    employeeAdapter.differ.submitList(employeeList)
                } else {
                    // Handle API failure (e.g. empty response)
                    println("Error: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<ApiService.Employeeinfo>>, t: Throwable) {
                // Handle network failure
                t.printStackTrace()
            }
        })
    }


    private fun prepareRVForEmployeesAdapter() {
        employeeAdapter = EmployeesAdapter()
        binding.rvemployees.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = employeeAdapter
        }
    }


    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val alertDialog = builder.create()
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { _, _ ->
            val sharedPref =
                requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
            sharedPref.edit().putBoolean("isLoggedIn", false).apply()
            startActivity(Intent(requireContext(), SigninActivity::class.java))
            requireActivity().finish()
        }
        builder.setNegativeButton("No") { _, _ ->
            alertDialog.dismiss()
        }
            .show()
            .setCancelable(false)
    }


}