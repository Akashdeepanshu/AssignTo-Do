package com.example.to_doapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.to_doapp.databinding.ItemViewEmployeesProfileBinding
import com.example.to_doapp.network.ApiService

class EmployeesAdapter : RecyclerView.Adapter<EmployeesAdapter.EmployeeViewHolder>() {

    class EmployeeViewHolder(val binding: ItemViewEmployeesProfileBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<ApiService.Employeeinfo>() {
        override fun areItemsTheSame(
            oldItem: ApiService.Employeeinfo,
            newItem: ApiService.Employeeinfo
        ): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: ApiService.Employeeinfo,
            newItem: ApiService.Employeeinfo
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val binding = ItemViewEmployeesProfileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EmployeeViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = differ.currentList[position]
        holder.binding.employeename.text = employee.name
    }
}
