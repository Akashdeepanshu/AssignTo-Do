// File: com.example.to_doapp.data.Task.kt

package com.example.to_doapp.data

data class Task(
    val user_id: String,
    val title: String,
    val completed: Boolean = false
)
