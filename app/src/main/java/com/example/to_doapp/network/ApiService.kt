package com.example.to_doapp.network
import android.os.Parcelable
import com.example.to_doapp.data.Task
import kotlinx.parcelize.Parcelize
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {


    data class RegisterUserRequest(
        val name: String,
        val email: String,
        val password: String,
        val user_type: String,
        val image_url: String? // Optional
    )

    @POST("/register")
    fun registerUser(@Body request: RegisterUserRequest): Call<Void>

    data class LoginRequest(
        val email: String,
        val password: String
    )
@Parcelize
    data class LoginResponse(
        val message: String,
        val user_type: String,
        val user_id: String,
        val name: String,
        val email: String,
        val image_url: String?
    ) : Parcelable

    data class Task(
        val title: String,
        val description: String,
        val lastDate: String,
        val priority: String,
        val employeeId: String,
        val workRoom: String,
        val workStatus: String
    )


    data class ForgotPasswordRequest(
        val email: String
    )
@Parcelize
    data class Employeeinfo(
        val _id: String = "",
        val name: String = "",
        val email: String = "",
        val password: String = "",
    ) : Parcelable

    @Parcelize
    data class Taskinfo(
        val _id: String = "",
        val title: String = "",
        val description: String = "",
        val lastDate: String = "",
        val priority: String = "",
        val employeeId: String = "",
        val workRoom: String = "",
        val workStatus: String = ""
    ) : Parcelable

    @GET("/get_employees")
    fun getAllEmployeeData(): Call<List<Employeeinfo>>


    @POST("/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>


    @POST("/forgot_password")
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<Void>


    @POST("/add_task")
    fun addTask(@Body task: Task): Call<ResponseBody>

    @DELETE("/unassign_task/{task_id}")
    fun unassignTask(@Path("task_id") taskId: String): Call<Void>


    @GET("get_tasks/{user_id}")
    fun getTasks(@Path("user_id") userId: String): Call<List<Taskinfo>>

    @PUT("/update_task_status/{task_id}")
    fun updateTaskStatus(
        @Path("task_id") taskId: String,
        @Body statusBody: Map<String, String>
    ): Call<Void>


}

