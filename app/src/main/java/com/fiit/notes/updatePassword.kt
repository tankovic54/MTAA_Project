package com.fiit.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class updatePassword : AppCompatActivity() {
    lateinit var password: com.google.android.material.textfield.TextInputEditText
    lateinit var confirm: com.google.android.material.textfield.TextInputEditText
    lateinit var confirmButton: Button
    lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)
        password = findViewById(R.id.change_password_update)
        confirm = findViewById(R.id.confirm_password_update)
        confirmButton = findViewById(R.id.passButton)
        backButton = findViewById(R.id.backButton_pass)
        confirmButton.setOnClickListener {
           val password_string = password.text.toString()
           val confirm_string = confirm.text.toString()
            if (password_string != confirm_string){
                confirm.error = "Passwords do not match"
                return@setOnClickListener
            }
            if (password_string.length < 6){
                password.error = "Minimum 6 characters"
                return@setOnClickListener
            }
            val goBack = Intent(this, settings::class.java)
            startActivity(goBack)
        }
        backButton.setOnClickListener {
            val goBack = Intent(this, settings::class.java)
            startActivity(goBack)
        }

    }
}