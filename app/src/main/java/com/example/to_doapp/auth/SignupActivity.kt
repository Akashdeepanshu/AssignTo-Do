package com.example.to_doapp.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.example.to_doapp.Boss
import com.example.to_doapp.Employee
import com.example.to_doapp.MainActivity
import com.example.to_doapp.R
import com.example.to_doapp.databinding.ActivitySignupBinding
import com.example.to_doapp.utils.utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private var userImageUri: Uri? = null
    private var userType: String = ""

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        userImageUri = it
        binding.UserImage.setImageURI(userImageUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.UserImage.setOnClickListener {
            selectImage.launch("image/*")
        }


        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            userType = findViewById<RadioButton>(checkedId).text.toString()
        }


        binding.btnregister.setOnClickListener {

            val name = binding.etNameInput.text.toString()
            val email = binding.etEmailInput.text.toString()
            val password = binding.etPasswordInput.text.toString()
            val confirmPassword = binding.etConfirmPasswordInput.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || userType.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            createUser(name, email, password)
        }
    }

    private fun createUser(name: String, email: String, password: String) {
//        utils.showDialog(this)

        lifecycle.coroutineScope.launch {
            try {
                val authResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
                val uid = authResult.user?.uid

                if (uid != null) {
                    val database = FirebaseDatabase.getInstance()
                    val userRef = if (userType == "Boss") {
                        database.getReference("Boss").child(uid)
                    } else {
                        database.getReference("Employee").child(uid)
                    }

                    val userObject = if (userType == "Boss") {
                        Boss(uid, name, email, password)
                    } else {
                        Employee(uid, name, email, password)
                    }

                    userRef.setValue(userObject).await()

//                    utils.hideDialog()
                    Toast.makeText(this@SignupActivity, "User Registered Successfully", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                    finish()
                } else {
//                    utils.hideDialog()
                    Toast.makeText(this@SignupActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
//                utils.hideDialog()
                Toast.makeText(this@SignupActivity, e.message ?: "Something went wrong", Toast.LENGTH_LONG).show()
                Log.e("SignupActivity", "Error: ${e.message}", e)
            }
        }
    }
}
