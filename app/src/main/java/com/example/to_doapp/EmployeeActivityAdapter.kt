package com.example.to_doapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.to_doapp.databinding.ItemViewEmployeesProfileBinding
import com.example.to_doapp.databinding.ItemViewEmpworkFragmentBinding
import com.example.to_doapp.databinding.ItemViewTaskFragmentBinding
import com.example.to_doapp.network.ApiService

class EmployeeActivityAdapter(
    private val onStatusChangeClick: (String, String) -> Unit // Callback to change status
) : RecyclerView.Adapter<EmployeeActivityAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(val binding: ItemViewEmpworkFragmentBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<ApiService.Taskinfo>() {
        override fun areItemsTheSame(oldItem: ApiService.Taskinfo, newItem: ApiService.Taskinfo): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: ApiService.Taskinfo, newItem: ApiService.Taskinfo): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemViewEmpworkFragmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = differ.currentList[position]
        with(holder.binding) {
            textViewWorkTitle.text = task.title
            textViewDate.text = task.lastDate
            textViewDescription.text = task.description
            when(task.priority){
                "3" -> isOval.setImageResource(R.drawable.redcircle)
                "2" -> isOval.setImageResource(R.drawable.yellowcircle)
                "1" -> isOval.setImageResource(R.drawable.greencircle)
            }
            when (task.workStatus){
                "1" -> holder.binding.textViewStatus.text = "Pending"
                "2" -> holder.binding.textViewStatus.text = "In Progress"
                "3" -> holder.binding.textViewStatus.text = "Completed"
            }

            root.setOnClickListener {
                layoutTaskDetails.visibility =
                    if (layoutTaskDetails.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }


            InProgressbtn.setOnClickListener {
                onStatusChangeClick(task._id, "2")
            }
            Completedbtn.setOnClickListener {
                onStatusChangeClick(task._id, "3")
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}

