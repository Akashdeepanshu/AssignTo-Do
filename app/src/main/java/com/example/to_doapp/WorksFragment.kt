@file:Suppress("DEPRECATION")

package com.example.to_doapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsAnimation
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.to_doapp.databinding.FragmentWorksBinding
import com.example.to_doapp.network.ApiClient
import com.example.to_doapp.network.ApiService


class WorksFragment : Fragment() {

    val employedetails by navArgs<WorksFragmentArgs>()
    private lateinit var taskAdapter: TaskAdapter
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
        prepareRVForTasksAdapter()
        showallTask()
        return binding.root


    }

    private fun showallTask() {
        val employeeId = employedetails.employeeData._id
        val apiService = ApiClient.instance

        apiService.getTasks(employeeId).enqueue(object : retrofit2.Callback<List<ApiService.Taskinfo>> {
            override fun onResponse(
                call: retrofit2.Call<List<ApiService.Taskinfo>>,
                response: retrofit2.Response<List<ApiService.Taskinfo>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        taskAdapter.differ.submitList(it)
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch tasks", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<List<ApiService.Taskinfo>>, t: Throwable) {

            }
        })
    }


    private fun prepareRVForTasksAdapter() {
        taskAdapter = TaskAdapter { task ->
            ApiClient.instance.unassignTask(task._id).enqueue(object : retrofit2.Callback<Void> {
                override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Task unassigned", Toast.LENGTH_SHORT).show()
                        val updatedList = taskAdapter.differ.currentList.toMutableList()
                        updatedList.remove(task)
                        taskAdapter.differ.submitList(updatedList)
                    } else {
                        Toast.makeText(requireContext(), "Unassign failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.rvtasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
        }
    }



    }



