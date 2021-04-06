package com.fiit.notes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class Login : AppCompatActivity() {

    lateinit var mail: com.google.android.material.textfield.TextInputEditText
    lateinit var pass: com.google.android.material.textfield.TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val regButton = findViewById<Button>(R.id.registration_button)
        regButton.setOnClickListener{
            val goToReg = Intent(this, RegisterClass:: class.java)
            startActivity(goToReg)
        }
        mail = findViewById(R.id.email_input)
        pass = findViewById(R.id.password_input)
        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener(){
            val email = mail.text.toString()
            val password = pass.text.toString()
            if (email.isEmpty()){
                mail.error = "Please enter email"
                return@setOnClickListener
            }

            if (password.isEmpty()){
                pass.error = "Please enter password"
                return@setOnClickListener
            }
        }
    }
}