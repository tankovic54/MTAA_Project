package com.fiit.notes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask

class RegisterClass : AppCompatActivity() {
    lateinit var username: com.google.android.material.textfield.TextInputEditText
    lateinit var mail: com.google.android.material.textfield.TextInputEditText
    lateinit var pass: com.google.android.material.textfield.TextInputEditText
    lateinit var buttonRegister : Button
    lateinit var profilePicture : ImageView
    lateinit var changePic : Button
    val RequestCode = 438
    var imageUri: Uri? = null
    var storageRef: StorageReference? = null

    private fun uploadImageToDatabase(){
        if (imageUri != null){
            val fileRef = storageRef!!.child(System.currentTimeMillis().toString() + ".jpg")
            val uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(imageUri!!)
            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot,Task<Uri>>{ task ->
                if (!task.isSuccessful) {
                    task.exception.let {
                        throw it!!
                    }
                }
                    return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()
                }
            }
        }

    }


    private fun register(){

        val name = username.text.toString()
        if (name.isEmpty()){
            username.error = "Please enter username"
            return
        }
        val mail = mail.text.toString()
        val password = pass.text.toString()
        val dbRef = FirebaseDatabase.getInstance().getReference("Users")
        val userId = dbRef.push().key
        val user = userId?.let { SaveInfo(it, name, mail, password) }
        if (userId != null) {
            dbRef.child(userId).setValue(user).addOnCompleteListener {
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
        profilePicture = findViewById(R.id.profile_picture)
        buttonRegister = findViewById(R.id.buttonReg)
        changePic = findViewById(R.id.change_picture)
        storageRef = FirebaseStorage.getInstance().reference.child("User Images")
        changePic.setOnClickListener(View.OnClickListener {
            fun onClick(){
              val openGallery = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, RequestCode)
            fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
                super.onActivityResult(requestCode, resultCode, data)
                if (requestCode == RequestCode && resultCode == Activity.RESULT_OK ){
                    if (data != null) {
                        imageUri = data.data
                        Toast.makeText(applicationContext, "Uploading", Toast.LENGTH_LONG).show()
                        uploadImageToDatabase()
                    }
                }
            }

            }
        })
        buttonRegister.setOnClickListener {
            register()
        }


    }
}