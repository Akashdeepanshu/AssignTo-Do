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
import com.example.to_doapp.auth.SigninActivity
import com.example.to_doapp.databinding.FragmentEmployeesBinding


class employeesFragment : Fragment() {

    private lateinit var binding: FragmentEmployeesBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEmployeesBinding.inflate(inflater, container, false)



        binding.tbEmployee.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout -> {
                    showLogoutDialog()
                    true
                }
                else -> false
            }
        }

        binding.btn1.setOnClickListener {
            findNavController().navigate(R.id.action_employeesFragment_to_worksFragment)
        }

        return binding.root
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