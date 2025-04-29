@file:Suppress("DEPRECATION")

package com.example.to_doapp

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.navigation.fragment.navArgs
import com.example.to_doapp.databinding.FragmentAssignWorkBinding
import com.example.to_doapp.databinding.FragmentWorksBinding
import com.example.to_doapp.network.ApiClient
import com.example.to_doapp.network.ApiService
import com.google.firebase.installations.Utils
import io.grpc.okhttp.internal.Util
import java.text.SimpleDateFormat


class assignWorkFragment : Fragment() {


    private lateinit var binding: FragmentAssignWorkBinding
    val employedetails by navArgs<assignWorkFragmentArgs>()
    private var priority = "1"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAssignWorkBinding.inflate(layoutInflater)

        binding.TbEmployeeAS.apply {
            setNavigationOnClickListener { activity?.onBackPressed() }
        }


        setPriority()
        setDate()
        binding.btnDone.setOnClickListener {

            val title = binding.ettitle.text.toString()
            val desc = binding.etWorkDesc.text.toString()
            val lastdate = binding.lastdate.text.toString()

            if (title.isEmpty()) {
                binding.ettitle.error = "Title is required"
            } else if (desc.isEmpty()) {
                binding.etWorkDesc.error = "Description is required"
            } else if (lastdate.isEmpty()) {
                binding.lastdate.error = "Last Date is required"
            } else {

                val empId = employedetails.employeedetails._id


                val task = ApiService.Task(
                    title = title,
                    description = desc,
                    lastDate = lastdate,
                    priority = priority,
                    employeeId = empId,
                    workRoom = "",
                    workStatus = "1"
                )

                val apiService = ApiClient.instance
                val call = apiService.addTask(task)

                call.enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
                    override fun onResponse(
                        call: retrofit2.Call<okhttp3.ResponseBody>,
                        response: retrofit2.Response<okhttp3.ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Task Assigned Successfully!", Toast.LENGTH_SHORT).show()
                            activity?.onBackPressed()
                        } else {
                            Toast.makeText(requireContext(), "Failed to assign task", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        return binding.root
    }

    private fun setDate() {
        val myCalender = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth -> myCalender.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalender)
        }

        }
        binding.datepicker.setOnClickListener{
            DatePickerDialog(requireContext(),datePicker,myCalender.get(Calendar.YEAR),myCalender.get(Calendar.MONTH),myCalender.get(Calendar.DAY_OF_MONTH)).show()
        }

    }

    private fun updateLable(myCalender: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat)
        binding.lastdate.text = sdf.format(myCalender.time)
    }

    private fun setPriority() {
       binding.apply {
           greenOval.setOnClickListener {
               Toast.makeText(requireContext(), "Priority is Low", Toast.LENGTH_SHORT).show()
               priority = "1"
               binding.greenOval.setImageResource(R.drawable.baseline_done_24)
               binding.yellowOval.setImageResource(0)
               binding.redOval.setImageResource(0)

           }
           yellowOval.setOnClickListener {
               Toast.makeText(requireContext(), "Priority is Medium", Toast.LENGTH_SHORT).show()

               priority = "2"
               binding.yellowOval.setImageResource(R.drawable.baseline_done_24)
               binding.greenOval.setImageResource(0)
               binding.redOval.setImageResource(0)
           }
           redOval.setOnClickListener{
               Toast.makeText(requireContext(), "Priority is High", Toast.LENGTH_SHORT).show()
               priority = "3"
               binding.redOval.setImageResource(R.drawable.baseline_done_24)
               binding.yellowOval.setImageResource(0)
               binding.greenOval.setImageResource(0)
           }

       }
    }


}