package com.fiit.notes

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class RegisterClass : AppCompatActivity() {
    lateinit var username: EditText
    lateinit var mail: com.google.android.material.textfield.TextInputEditText
    lateinit var pass: com.google.android.material.textfield.TextInputEditText
    lateinit var buttonRegister : Button
    lateinit var profilePicture : ImageView
    lateinit var changePic : Button
    var storageRef: StorageReference? = null
    lateinit var auth: FirebaseAuth
    var fileUri : Uri? = null

     fun register(){

        auth = FirebaseAuth.getInstance()
        val name = username.text.toString()
        if (name.isEmpty()){
            username.error = "Please enter username"
            return
        }
        val email = mail.text.toString()
         if (email.isEmpty()){
             mail.error = "Please enter email"
             return
         }

         val password = pass.text.toString()
         if (password.isEmpty()){
             pass.error = "Please enter password"
             return
         }
        val dbRef = FirebaseDatabase.getInstance().getReference("Users")
        val userId = dbRef.push().key

        val user = userId?.let { SaveInfo(it, name, email, password) }
         auth.createUserWithEmailAndPassword(email, password)
         auth.currentUser.let {
             val update = UserProfileChangeRequest.Builder()
                 .setPhotoUri(fileUri)
                 .build()
             it?.updateProfile(update)
         }
        if (userId != null) {
            dbRef.child(userId).setValue(user).addOnCompleteListener {
                Toast.makeText(applicationContext,"User saved successfully", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun selectImage(){
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
        profilePicture = findViewById(R.id.profile_picture)
        buttonRegister = findViewById(R.id.buttonReg)
        changePic = findViewById(R.id.change_picture)
        storageRef = FirebaseStorage.getInstance().reference.child("User Images")
        changePic.setOnClickListener {

        }
        changePic.setOnClickListener{
            selectImage()
        }
        buttonRegister.setOnClickListener {
            register()
            val goToLogin = Intent(this, Login:: class.java)
            startActivity(goToLogin)
        }
        val buttonBack = findViewById<Button>(R.id.backButton)
        buttonBack.setOnClickListener{
            val goToLogin = Intent(this, Login:: class.java)
            startActivity(goToLogin)
        }

    }
}


