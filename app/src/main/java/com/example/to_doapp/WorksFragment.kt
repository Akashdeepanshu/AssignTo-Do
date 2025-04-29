@file:Suppress("DEPRECATION")

package com.example.to_doapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.to_doapp.databinding.FragmentWorksBinding


class WorksFragment : Fragment() {

    val employedetails by navArgs<WorksFragmentArgs>()

    private lateinit var binding: FragmentWorksBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWorksBinding.inflate(layoutInflater)

        binding.fabAssignWork.setOnClickListener {
            val action = WorksFragmentDirections.actionWorksFragmentToAssignWorkFragment(
                employedetails.employeeData,
            )
            findNavController().navigate(action)
        }
        val employee = employedetails.employeeData
        binding.tbEmployee.apply {
            title = employee.name
            setNavigationOnClickListener {activity?.onBackPressed()}
        }
        return binding.root


    }



}