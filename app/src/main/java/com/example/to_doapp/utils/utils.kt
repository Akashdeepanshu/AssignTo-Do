package com.example.to_doapp.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.to_doapp.R

object utils {
    private var dialog : AlertDialog? = null

    fun showDialog(context : Context) {
        dialog = AlertDialog.Builder(context).setView(R.layout.progress_dialog).setCancelable(false).create()
        dialog!!.show()

    }
    fun hideDialog(){
        dialog!!.dismiss()
    }
}