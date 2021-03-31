package com.fiit.notes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class settings : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var displayName: TextView
    lateinit var profilePic: ImageView
    lateinit var signout: Button
    lateinit var deleteacc: Button
    lateinit var changePass: Button
    lateinit var changeEmail: Button
    lateinit var changeName: Button
    lateinit var changePicture: Button
    var fileUri : Uri? = null

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
                profilePic.setImageURI(fileUri)
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
        setContentView(R.layout.activity_settings)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val backButton = findViewById<Button>(R.id.backbutton_settings)
        backButton.setOnClickListener {
            val goHome = Intent(this, Homepage::class.java)
            startActivity(goHome)
        }

        signout = findViewById<Button>(R.id.sign_out)
        signout.setOnClickListener {
            auth.signOut()
            val goLog = Intent(this, Login::class.java)
            startActivity(goLog)
        }
        deleteacc = findViewById(R.id.delete_acc)
        deleteacc.setOnClickListener {
            user.delete()
            val goLog = Intent(this, Login::class.java)
            startActivity(goLog)
        }

        profilePic = findViewById<ImageView>(R.id.profile_picture)

        displayName = findViewById<TextView>(R.id.displayname)
        user?.let {
            for (profile in it.providerData) {

                // Name, email address, and profile photo Url
                val name = profile.displayName
                val email = profile.email
                val photoUrl = profile.photoUrl
                displayName.text = name
                profilePic.setImageURI(photoUrl)
            }
        }
        changePass = findViewById(R.id.change_password_settings)
        changePass.setOnClickListener {
            val password = Intent(this, updatePassword::class.java)
            startActivity(password)
        }
        changeEmail = findViewById(R.id.change_email_settings)
        changeEmail.setOnClickListener {
            val changeName = Intent(this, updateEmail::class.java)
            startActivity(changeName)
        }
        changeName = findViewById(R.id.change_name_settings)
        changeName.setOnClickListener {
            val changeName = Intent(this, updateName::class.java)
            startActivity(changeName)
        }
        changePicture = findViewById(R.id.change_picture_settings)
        changePicture.setOnClickListener {
            selectImage()
            if (fileUri == null){
                fileUri = Uri.parse("android.resource://com.fiit.notes/34AD2")
            }
            user.let {
                val update = UserProfileChangeRequest.Builder()
                    .setPhotoUri(fileUri)
                    .build()
                it?.updateProfile(update)
            }
            val goBack = Intent(this, settings::class.java)
            startActivity(goBack)
        }

    }
}