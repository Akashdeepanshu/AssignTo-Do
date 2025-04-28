package com.example.to_doapp.network
import com.example.to_doapp.data.Task
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

    data class LoginResponse(
        val message: String,
        val user_type: String,
        val user_id: String,
        val name: String,
        val email: String,
        val image_url: String?
    )

    data class ForgotPasswordRequest(
        val email: String
    )

    data class Employeeinfo(
        val _id: String = "",
        val name: String = "",
        val email: String = "",
        val password: String = "",
    )

    @GET("/get_employees")
    fun getAllEmployeeData(): Call<List<Employeeinfo>>


    @POST("/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>


    @POST("/forgot_password")
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<Void>


    @POST("add_task")
    fun addTask(@Body task: Task): Call<ResponseBody>

    @GET("get_tasks/{user_id}")
    fun getTasks(@Path("user_id") userId: String): Call<List<Task>>
}

