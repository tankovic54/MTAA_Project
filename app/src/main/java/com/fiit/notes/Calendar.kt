package com.fiit.notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Calendar : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        val userID = bundle!!.getString("userID")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
    }
}