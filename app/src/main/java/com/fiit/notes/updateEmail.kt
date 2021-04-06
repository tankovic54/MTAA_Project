package com.fiit.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button

class updateEmail : AppCompatActivity() {
    lateinit var backButton: Button
    lateinit var emailInput: com.google.android.material.textfield.TextInputEditText
    lateinit var mailButton: Button

    private fun isEmailValid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_email)
        backButton = findViewById(R.id.backButton_email)
        mailButton = findViewById(R.id.emailButton)

        mailButton.setOnClickListener {
            val email = emailInput.text.toString()
            if (email.isEmpty()){
                emailInput.error = "Please enter email"
                return@setOnClickListener
            }
            val validMail = isEmailValid(email)
            if (!validMail){
                emailInput.error = "Please enter valid email address"
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