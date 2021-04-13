package com.fiit.notes

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
import java.time.format.DateTimeParseException

class CreateNote: AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun isValidIsoDateTime(date: String?): Boolean {
        return try {
            DateTimeFormatter.ISO_DATE.parse(date)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    var FavButton: Boolean = false
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        var noteIDexists = false
        var noteID: String? = null
        val userID = bundle!!.getString("userID")
        if(bundle.containsKey("noteID")){
            noteID = bundle.getString("noteID")
            noteIDexists = true
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnote)

        val cancelBtn = findViewById<Button>(R.id.cancelBtn)
        cancelBtn.setOnClickListener{
            val goToHomepage = Intent(this, Homepage:: class.java)
            goToHomepage.putExtra("userID",userID)
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
        if(noteIDexists == true){
            /*val xx = "hahaha"
            note_name.setText(xx)*/
            val urlGetNote = "http://10.0.2.2:8080/api/v1/notes/$noteID"
            val queueH = Volley.newRequestQueue(this)
            val stringRequest = StringRequest(Request.Method.GET, urlGetNote,{
                response ->

                val answer = response.toString()
                val obj1 = JSONObject(answer)

                val nameHelp = obj1["note"].toString()
                note_name.setText(nameHelp)
                val odHelp = obj1["fromDate"].toString()
                od.setText(odHelp)
                val platnostHelp = obj1["toDate"].toString()
                platnost.setText(platnostHelp)
                val textHelp = obj1["description"].toString()
                textNote.setText(textHelp)
                //System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
            },
                    {Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show() })
            queueH.add(stringRequest)

        }
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

            if (!isValidIsoDateTime(datum_od)){
                od.error = "Please use YYYY-MM-DD format"
                return@setOnClickListener
            }

            if (!isValidIsoDateTime(datum_do)){
                platnost.error = "Please use YYYY-MM-DD format"
                return@setOnClickListener
            }

            val url = "http://10.0.2.2:8080/api/v1/notes/newNote"
            val queue = Volley.newRequestQueue(this)
            val noteData = JSONObject()
            try {
                noteData.put("note",meno )
                noteData.put("description", description)
                noteData.put("favourite", FavButton)
                noteData.put("fromDate", datum_od)
                noteData.put("toDate", datum_do)
                noteData.put("user_id", userID)
            } catch (e: JSONException) {
                e.printStackTrace()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, noteData, Response.Listener<JSONObject?>
            { response -> Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
                val goToHomepage = Intent(this, Homepage:: class.java)
                goToHomepage.putExtra("userID",userID)
                startActivity(goToHomepage) },
                    Response.ErrorListener { error -> Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show() })

            queue.add(jsonObjectRequest)
        }

    }

}