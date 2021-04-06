package com.fiit.notes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class RegisterClass : AppCompatActivity() {
    lateinit var username: EditText
    lateinit var mail: com.google.android.material.textfield.TextInputEditText
    lateinit var pass: com.google.android.material.textfield.TextInputEditText
    lateinit var confirmPassword: com.google.android.material.textfield.TextInputEditText
    lateinit var buttonRegister : Button
    lateinit var profilePicture : ImageView
    lateinit var changePic : Button
    var storageRef: StorageReference? = null
    var fileUri : Uri? = null

    private fun isEmailValid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun selectImage(){
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode){
            Activity.RESULT_OK -> {
                fileUri = data?.data
                profilePicture.setImageURI(fileUri)
            }
            ImagePicker.RESULT_ERROR ->{
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        username = findViewById(R.id.username)
        mail = findViewById(R.id.email)
        pass = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirm_password)
        profilePicture = findViewById(R.id.profile_picture)
        buttonRegister = findViewById(R.id.buttonReg)
        changePic = findViewById(R.id.change_picture)
        storageRef = FirebaseStorage.getInstance().reference.child("User Images")
        changePic.setOnClickListener {

        }
        changePic.setOnClickListener{
            selectImage()
        }
        val buttonBack = findViewById<Button>(R.id.backButton)
        buttonBack.setOnClickListener{
            val goToLogin = Intent(this, Login:: class.java)
            startActivity(goToLogin)
        }
    }
}


