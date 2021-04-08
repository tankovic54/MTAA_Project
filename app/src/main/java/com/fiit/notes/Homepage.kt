package com.fiit.notes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


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
        val cards = findViewById<androidx.cardview.widget.CardView>(R.id.note_card)
        val url = "http://10.0.2.2:8080/api/v1/notes/$userID"
        //val jsonResponses: MutableList<String> = ArrayList()
//
        //val requestQueue = Volley.newRequestQueue(this)
        //val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener<JSONObject?> {
        //    response -> fun onResponse(response: JSONObject) {
        //        try {
        //            val jsonArray = response.getJSONArray("data")
        //            for (i in 0 until jsonArray.length()) {
        //                val jsonObject = jsonArray.getJSONObject(i)
        //                val note = jsonObject.getString("note_name")
        //                jsonResponses.add(note)
        //            }
        //        } catch (e: JSONException) {
        //            e.printStackTrace()
        //            Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show()
        //        }
        //    }
        //}, Response.ErrorListener { error -> Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show() })
//
        //requestQueue.add(jsonObjectRequest)

    }
}