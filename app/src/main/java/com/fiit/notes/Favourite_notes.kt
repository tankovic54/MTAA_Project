package com.fiit.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class Favourite_notes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        val userID = bundle!!.getString("userID")
        var emptyList = false
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_notes)

        val backBtn = findViewById<Button>(R.id.FavBackButton)
        backBtn.setOnClickListener {
            val home = Intent(this, Homepage:: class.java)
            home.putExtra("userID",userID)
            startActivity(home)
        }
        val noteList = findViewById<ListView>(R.id.fav_notes_list)
        val listID: MutableList<String> = ArrayList()
        var arrayAdapter: ArrayAdapter<String>? = null
        val list: MutableList<String> = ArrayList()
        val url = "http://10.0.2.2:8080/api/v1/notes/user/$userID"
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, url,
                { response ->
                    val answer = response.toString()
                    val obj = JSONArray(answer)
                    val pocetPoznamok = obj.length() - 1
                    if (obj.length() > 0){
                        for (index: Int in 0..pocetPoznamok){
                            val obj1 = JSONObject(obj[index].toString())
                            val favObj =  obj1["favourite"].toString()
                            val noteName = obj1["note"].toString()
                            val noteID = obj1["id"].toString()
                            if (favObj == "true"){
                                list.add(noteName)
                                listID.add(noteID)
                            }
                        }
                    }
                    if (list.isEmpty()){
                        val emptyNotes = "No favourite notes"
                        list.add(emptyNotes)
                        emptyList = true
                    }
                    arrayAdapter= ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
                    noteList.adapter = arrayAdapter
                },
                { Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show() })

        queue.add(stringRequest)

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