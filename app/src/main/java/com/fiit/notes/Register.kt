package com.fiit.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class RegisterClass : AppCompatActivity() {
    lateinit var username: com.google.android.material.textfield.TextInputEditText
    lateinit var mail: com.google.android.material.textfield.TextInputEditText
    lateinit var pass: com.google.android.material.textfield.TextInputEditText
    lateinit var buttonRegister : Button

    private fun register(){
        val name = username.text.toString()
        if (name.isEmpty()){
            username.error = "Please enter username"
            return
        }
        val mail = mail.text.toString()
        val password = pass.text.toString()
//        val registration_instance = SaveInfo(name, mail, password)
        val dbRef = FirebaseDatabase.getInstance().getReference("Users")
        val user_id = dbRef.push().key
        val user = user_id?.let { SaveInfo(it, name, mail, password) }
        if (user_id != null) {
            dbRef.child(user_id).setValue(user).addOnCompleteListener {
                Toast.makeText(applicationContext,"User saved successfully", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        username = findViewById(R.id.username)
        mail = findViewById(R.id.email)
        pass = findViewById(R.id.password)
        buttonRegister = findViewById(R.id.buttonReg)
        buttonRegister.setOnClickListener {
            register()
        }

    }
}