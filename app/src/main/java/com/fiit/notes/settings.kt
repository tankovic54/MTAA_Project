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
import androidx.core.net.toUri
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.switchmaterial.SwitchMaterial
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Settings : AppCompatActivity() {
    lateinit var displayName: TextView
    lateinit var profilePic: ImageView
    lateinit var signout: Button
    lateinit var deletenotes : Button
    lateinit var deleteacc: Button
    lateinit var changeDetails: Button
    lateinit var changePicture: Button
    lateinit var backBtn : Button
    lateinit var darkModeToggle : MaterialButtonToggleGroup
    var fileUri : Uri? = null

    private fun selectImage(){
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()
    }

    private fun changeImage(imagePath: String)
    {
        val bundle = intent.extras
        val userID = bundle!!.getString("userID")
        val nameUrl = "http://10.0.2.2:8080/api/v1/users/$userID"
        val queue = Volley.newRequestQueue(this)
        val updateData = JSONObject()
        try {
            updateData.put("image", imagePath)
        }
        catch (e: JSONException) {
            e.printStackTrace()
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
        val jsonObjectRequest = JsonObjectRequest(Request.Method.PUT, nameUrl, updateData, {
            response -> Toast.makeText(this, "Image updated", Toast.LENGTH_SHORT).show() }, {
            error -> Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            })
        queue.add(jsonObjectRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode){
            Activity.RESULT_OK -> {
                fileUri = data?.data
                profilePic.setImageURI(fileUri)
                changeImage(fileUri.toString())
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

        displayName = findViewById(R.id.displayname)
        val nameUrl = "http://10.0.2.2:8080/api/v1/users/$userID"
        val nameQueue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, nameUrl,
                { response ->
                    val answer = response.toString()
                    val obj = JSONObject(answer)
                    displayName.text = "Hello " + obj["name"].toString()
                    profilePic.setImageURI(obj["image"].toString().toUri())
                },
                { Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show() })
        nameQueue.add(stringRequest)

        val backButton = findViewById<Button>(R.id.backBtn_settings)
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
                    { response ->
                        Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show()
                    },
                    {
                        Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show()
                    })
            queue.add(deleteRequest)
            val goHome = Intent(this, Homepage::class.java)
            startActivity(goHome)
        }

        deleteacc = findViewById(R.id.delete_acc)
        deleteacc.setOnClickListener {
            val url = "http://10.0.2.2:8080/api/v1/users/$userID"
            val queue = Volley.newRequestQueue(this)
            val deleteRequest = StringRequest(Request.Method.DELETE, url,
                    { response ->
                        Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show()
                    },
                    {
                        Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show()
                    })
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
        backBtn = findViewById(R.id.backBtn_settings)
        backBtn.setOnClickListener {
            val goToHomepage = Intent(this, Homepage:: class.java)
            goToHomepage.putExtra("userID",userID)
            startActivity(goToHomepage)
        }
    }
}