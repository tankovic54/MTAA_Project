package com.fiit.notes

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject


class Homepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        val userID = bundle!!.getString("userID")
        var emptyList = false

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
        val settingBtn = findViewById<Button>(R.id.settings)
        settingBtn.setOnClickListener {
            val settingScreen = Intent(this, settings:: class.java)
            settingScreen.putExtra("userID",userID)
            startActivity(settingScreen)
        }
        val noteList = findViewById<ListView>(R.id.note_card)
        val list: MutableList<String> = ArrayList()
        var listID: MutableList<String> = ArrayList()
        val url = "http://10.0.2.2:8080/api/v1/notes/user/$userID"
        val queue = Volley.newRequestQueue(this)
        var arrayAdapter: ArrayAdapter<String>? = null
        val stringRequest = StringRequest(Request.Method.GET, url,
                { response ->
                    val answer = response.toString()
                    val obj = JSONArray(answer)
                    val pocetPoznamok = obj.length() - 1
                    if (obj.length() > 0){
                        for (index: Int in 0..pocetPoznamok){
                            val obj1 = JSONObject(obj[index].toString())
                            val noteName = obj1["note"].toString()
                            val noteID = obj1["id"].toString()
                            listID.add(noteID)
                            list.add(noteName)
                        }
                    }
                    else{
                        val emptyNotes = "Add some notes"
                        list.add(emptyNotes)
                        emptyList = true
                    }
                    arrayAdapter= ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
                    noteList.adapter = arrayAdapter
                },
                { Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show() })

        queue.add(stringRequest)

        val favButton = findViewById<Button>(R.id.home_fav_button)
        favButton.setOnClickListener {
            val favNotes = Intent(this, Favourite_notes:: class.java)
            favNotes.putExtra("userID",userID)
            startActivity(favNotes)
        }

        noteList.setOnItemClickListener { parent, view, position, id ->
            if (!emptyList) {
                val element = arrayAdapter?.getItem(position) // The item that was clicked
                val noteID = listID.get(position)
                val intent = Intent(this, CreateNote::class.java)
                intent.putExtra("userID", userID)
                intent.putExtra("noteID", noteID)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Create some note", Toast.LENGTH_SHORT).show()
                return@setOnItemClickListener
            }

        }
    }
}