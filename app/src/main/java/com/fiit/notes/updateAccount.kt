package com.fiit.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class UpdateAccount: AppCompatActivity() {
    lateinit var username: com.google.android.material.textfield.TextInputEditText
    lateinit var email: com.google.android.material.textfield.TextInputEditText
    lateinit var password: com.google.android.material.textfield.TextInputEditText
    lateinit var confirm: com.google.android.material.textfield.TextInputEditText
    lateinit var confirmButton: Button
    lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        val userID = bundle!!.getString("userID")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_account)

        username = findViewById(R.id.update_name)
        email = findViewById(R.id.update_email)
        password = findViewById(R.id.update_password)
        confirm = findViewById(R.id.confirm_password)
        confirmButton = findViewById(R.id.confirmButton)
        backButton = findViewById(R.id.backButton_pass)

        confirmButton.setOnClickListener {
           val password_string = password.text.toString()
           val confirm_string = confirm.text.toString()
            if (password_string != confirm_string){
                confirm.error = "Passwords do not match"
                return@setOnClickListener
            }

            val goBack = Intent(this, settings::class.java)
            goBack.putExtra("userID",userID)
            startActivity(goBack)
        }
        backButton.setOnClickListener {
            val goBack = Intent(this, settings::class.java)
            goBack.putExtra("userID",userID)
            startActivity(goBack)
        }

    }
}