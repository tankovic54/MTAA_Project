package com.fiit.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class updateName : AppCompatActivity() {
    lateinit var backButton: Button
    lateinit var nameInput: com.google.android.material.textfield.TextInputEditText
    lateinit var nameButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_name)
        backButton = findViewById(R.id.backButton_name)

        backButton.setOnClickListener {
            val goBack = Intent(this, settings::class.java)
            startActivity(goBack)
        }

        nameButton = findViewById(R.id.change_name_update)
        nameButton.setOnClickListener {
            val nameString = nameInput.text.toString()
            val goBack = Intent(this, settings::class.java)
            startActivity(goBack)
        }
    }
}