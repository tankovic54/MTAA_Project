package com.fiit.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class updateName : AppCompatActivity() {
    lateinit var backButton: Button
    lateinit var auth: FirebaseAuth
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
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        nameButton = findViewById(R.id.change_name_update)
        nameButton.setOnClickListener {
            val nameString = nameInput.text.toString()
            user.let {
                val update = UserProfileChangeRequest.Builder()
                    .setDisplayName(nameString)
                    .build()
                it?.updateProfile(update)
            }
            val goBack = Intent(this, settings::class.java)
            startActivity(goBack)
        }
    }
}