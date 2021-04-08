package com.fiit.notes

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text


class Homepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        val userID = bundle!!.getString("userID")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        val buttonBack = findViewById<Button>(R.id.homeBackButton)
        buttonBack.setOnClickListener{
            val goToLogin = Intent(this, Login:: class.java)
            startActivity(goToLogin)
        }
        val addNoteBtn = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.addNote)
        addNoteBtn.setOnClickListener{
            val createNote = Intent(this, CreateNote:: class.java)
            createNote.putExtra("userID",userID)
            startActivity(createNote)
        }
        val noteList = findViewById<ListView>(R.id.note_card)

        val url = "http://10.0.2.2:8080/api/v1/notes/$userID"
        val jsonResponses: ArrayList<String> = ArrayList()
    }
}