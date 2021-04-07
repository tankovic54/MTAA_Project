package com.fiit.notes

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE

class CreateNote: AppCompatActivity() {
    var FavButton: Boolean = false
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnote)

        val cancelBtn = findViewById<Button>(R.id.cancelBtn)
        cancelBtn.setOnClickListener{
            val goToHomepage = Intent(this, Homepage:: class.java)
            startActivity(goToHomepage)
        }
        val favourite = findViewById<Button>(R.id.favourite_button)
        favourite.setOnClickListener {
            FavButton = FavButton != true
            val toastMessage = FavButton.toString()
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }
        val note_name = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.note_name)
        val od = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.from)
        val platnost = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.to)
        val textNote = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.noteText)
        val saveNote = findViewById<Button>(R.id.saveBtn)
        saveNote.setOnClickListener {
            val meno = note_name.text.toString()
            val description = textNote.text.toString()
            val datum_od = od.text.toString()
            val datum_do = platnost.text.toString()

            if (meno.isEmpty()){
                note_name.error = "Please enter note name"
                return@setOnClickListener
            }

            if (description.isEmpty()){
                textNote.error = "Note can not be blank"
                return@setOnClickListener
            }

            if (datum_od.isEmpty()){
                od.error = "This field can not be empty"
                return@setOnClickListener
            }

            if (datum_do.isEmpty()){
                platnost.error = "This field can not be empty"
                return@setOnClickListener
            }

            val current = LocalDate.now().format(ISO_LOCAL_DATE)

            if (current.compareTo(datum_do) > 0){
                platnost.error = "Incorrect date"
                return@setOnClickListener
            }

            if (datum_od.compareTo(datum_do) > 0){
                od.error = "Incorrect date"
                return@setOnClickListener
            }

            val url = "http://10.0.2.2:8080/api/v1/notes/newNote"
            val queue = Volley.newRequestQueue(this)
            val loginData = JSONObject()
            try {
                loginData.put("name",meno )
                loginData.put("odkedy", datum_od)
                loginData.put("dokedy", datum_do)
                loginData.put("favourite", FavButton)
                loginData.put("description", description)
            } catch (e: JSONException) {
                e.printStackTrace()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, loginData, Response.Listener<JSONObject?>
            { response -> Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
                val goHome = Intent(this, Homepage:: class.java)
                startActivity(goHome) },
                    Response.ErrorListener { error -> Toast.makeText(this, "Required fields not filled", Toast.LENGTH_SHORT).show() })

            queue.add(jsonObjectRequest)
        }

    }

}