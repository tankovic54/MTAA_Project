package com.fiit.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val dbref = FirebaseDatabase.getInstance()

        val regButton = findViewById<Button>(R.id.registration_button)
            regButton.setOnClickListener{
                val goToReg = Intent(this, RegisterClass:: class.java)
                startActivity(goToReg)
            }

        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener(){

        }
    }
}