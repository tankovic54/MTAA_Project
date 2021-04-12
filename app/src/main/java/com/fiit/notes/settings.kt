package com.fiit.notes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.switchmaterial.SwitchMaterial

class settings : AppCompatActivity() {
    lateinit var displayName: TextView
    lateinit var profilePic: ImageView
    lateinit var signout: Button
    lateinit var deletenotes : Button
    lateinit var deleteacc: Button
    lateinit var changeDetails: Button
    lateinit var changePicture: Button
    lateinit var darkModeToggle : com.google.android.material.button.MaterialButtonToggleGroup
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
        val bundle = intent.extras
        val userID = bundle!!.getString("userID")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<Button>(R.id.backbutton_settings)
        backButton.setOnClickListener {
            val goHome = Intent(this, Homepage::class.java)
            startActivity(goHome)
        }

        signout = findViewById<Button>(R.id.sign_out)
        signout.setOnClickListener {
            Toast.makeText(this, "See you later alligator", Toast.LENGTH_SHORT).show()
            val goLog = Intent(this, Login::class.java)
            startActivity(goLog)
        }


        deletenotes = findViewById(R.id.delete_notes)
        deletenotes.setOnClickListener {
            val url = "http://10.0.2.2:8080/api/v1/notes/$userID"
            val queue = Volley.newRequestQueue(this)
            val deleteRequest = StringRequest(Request.Method.DELETE, url,
                    Response.Listener<String> { response ->
                        Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show()
                    },
                    Response.ErrorListener { Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show() })
            queue.add(deleteRequest)
            val goHome = Intent(this, Homepage::class.java)
            startActivity(goHome)
        }

        deleteacc = findViewById(R.id.delete_acc)
        deleteacc.setOnClickListener {
            val url = "http://10.0.2.2:8080/api/v1/users/$userID"
            val queue = Volley.newRequestQueue(this)
            val deleteRequest = StringRequest(Request.Method.DELETE, url,
                    Response.Listener<String> { response ->
                        Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show()
                    },
                    Response.ErrorListener { Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show() })
            queue.add(deleteRequest)
            val goLog = Intent(this, Login::class.java)
            goLog.putExtra("userID",userID)
            startActivity(goLog)
        }

        profilePic = findViewById(R.id.profile_picture)

        displayName = findViewById(R.id.displayname)

        changeDetails = findViewById(R.id.change_details_settings)

        changeDetails.setOnClickListener {
            val details = Intent(this, UpdateAccount::class.java)
            details.putExtra("userID",userID)
            startActivity(details)
        }

        changePicture = findViewById(R.id.change_picture_settings)
        changePicture.setOnClickListener {
            selectImage()
            if (fileUri == null){
                fileUri = Uri.parse("android.resource://com.fiit.notes/34AD2")
            }
            val goBack = Intent(this, settings::class.java)
            startActivity(goBack)
        }
        darkModeToggle = findViewById(R.id.theme_picker)
        darkModeToggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked){
                val theme = when(checkedId){
                    R.id.system -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    R.id.dark_mode -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_NO
                }
                AppCompatDelegate.setDefaultNightMode(theme)
            }
        }

    }
}