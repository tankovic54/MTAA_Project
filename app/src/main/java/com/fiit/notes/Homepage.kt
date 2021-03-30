package com.fiit.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Homepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        val buttonBack = findViewById<Button>(R.id.homeBackButton)
        buttonBack.setOnClickListener{
            val goToLogin = Intent(this, Login:: class.java)
            startActivity(goToLogin)
        }
    }
}