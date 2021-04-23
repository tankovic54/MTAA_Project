package com.fiit.notes

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.URL

class RegisterClass : AppCompatActivity() {
    lateinit var username: TextInputEditText
    lateinit var email: TextInputEditText
    lateinit var pass: TextInputEditText
    lateinit var confirmPassword: TextInputEditText
    lateinit var buttonRegister : Button
    lateinit var profilePicture : ImageView
    lateinit var changePic : Button
    var storageRef: StorageReference? = null
    var fileUri : Uri? = null
    lateinit var bm : Bitmap
    var byteArrayOS : ByteArrayOutputStream? = null
    lateinit var byteArray: ByteArray
    var encodedImage : String? = null
    private val pickImage = 100

    private fun isEmailValid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

//    private fun selectImage(){
//        ImagePicker.with(this)
////                .crop()
////                .compress(1024)
////                .maxResultSize(1080, 1080)
//                .start()
//    }

    private fun chooseFile(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null && data.data != null) {
            var os: ByteArrayOutputStream? = null
            fileUri = data.data
            bm = BitmapFactory.decodeStream(fileUri?.let { contentResolver.openInputStream(it) })
            bm.compress(Bitmap.CompressFormat.PNG, 100, os)
            Toast.makeText(this, bm.toString(), Toast.LENGTH_LONG).show()
//            //bm = BitmapFactory.decodeStream(fileUri?.let { contentResolver.openInputStream(it) })
//            bm.compress(Bitmap.CompressFormat.PNG, 100, os)

            //profilePicture.setImageBitmap(bm)
//            //profilePicture.setImageURI(fileUri)
//            bm = BitmapFactory.decodeStream(fileUri?.let { contentResolver.openInputStream(it) });
//            bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOS)
//            //bm = (profilePicture.drawable as BitmapDrawable).bitmap
//            //bm = BitmapFactory.decodeFile(fileUri.toString())
//            //bm.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS)
//            byteArray = byteArrayOS!!.toByteArray()
//            encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
//        super.onActivityResult(requestCode, resultCode, data)
//        when (resultCode){
//            RESULT_OK -> {
//                fileUri = data?.data
//                bm = BitmapFactory.decodeStream(fileUri?.let { contentResolver.openInputStream(it) });
//                bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOS)
//                byteArray = byteArrayOS!!.toByteArray()
//                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
//                profilePicture.setImageBitmap(bm)
//            }
//            ImagePicker.RESULT_ERROR ->{
//                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//            }
//            else -> {
//                Toast.makeText(this, "Task cancelled", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        username = findViewById(R.id.username)
        email = findViewById(R.id.email)
        pass = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirm_password)
        profilePicture = findViewById(R.id.profile_picture)
        buttonRegister = findViewById(R.id.buttonReg)
        changePic = findViewById(R.id.change_picture)
        storageRef = FirebaseStorage.getInstance().reference.child("User Images")

        changePic.setOnClickListener{
            //selectImage()
            chooseFile()
        }

        val buttonBack = findViewById<Button>(R.id.backButton)
        buttonBack.setOnClickListener{
            val goToLogin = Intent(this, Login::class.java)
            startActivity(goToLogin)
        }
        buttonRegister.setOnClickListener {
            val name = username.text.toString()
            val password = pass.text.toString()
            val confirm  = confirmPassword.text.toString()
            val mail = email.text.toString()

            if (mail.isEmpty()){
                email.error = "Please enter mail"
                return@setOnClickListener
            }

            if (password.isEmpty()){
                pass.error = "Please enter password"
                return@setOnClickListener
            }
            if (name.isEmpty()){
                username.error = "Please enter password"
                return@setOnClickListener
            }
            if (password != confirm){
                confirmPassword.error = "Passwords do not match"
                return@setOnClickListener
            }
            if (password.length < 6 ){
                pass.error = "Password has to have at least 6 characters"
                return@setOnClickListener
            }
//            if (encodedImage.equals(null)){
//
//            }
            val url = "http://10.0.2.2:8080/api/v1/users/register"
            val queue = Volley.newRequestQueue(this)
            val registerData = JSONObject()
            try {
                registerData.put("name", name)
                registerData.put("password", password)
                registerData.put("email", mail)
                registerData.put("image", encodedImage)
            } catch (e: JSONException) {
                e.printStackTrace()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
            val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.POST, url, registerData, { response ->
                Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()
                val goLogin = Intent(this, Login::class.java)
                startActivity(goLogin)
            },
                    { error -> Toast.makeText(this, "Wrong email, password or username", Toast.LENGTH_SHORT).show() })

            queue.add(jsonObjectRequest)

        }
    }
}



