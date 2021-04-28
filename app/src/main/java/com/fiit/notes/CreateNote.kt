package com.fiit.notes

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_addnote.view.*
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat.getInstance
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_DATE
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
import java.time.format.DateTimeParseException
import java.util.*
import java.util.Calendar

fun EditText.transformIntoDatePicker(context: Context, format: String) {
    isFocusableInTouchMode = false
    isClickable = true
    isFocusable = false
    val myCalendar = Calendar.getInstance()
    val datePickerOnDataSetListener =
            OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.GERMANY)
                setText(sdf.format(myCalendar.time))
            }

    setOnClickListener {
        DatePickerDialog(
                context, R.style.MyDatePickerDialogTheme, datePickerOnDataSetListener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)
        ).run {
            show()
        }
    }
}

class CreateNote: AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun isValidIsoDateTime(date: String?): Boolean {
        return try {
            ISO_DATE.parse(date)
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
            val goToHomepage = Intent(this, Homepage::class.java)
            goToHomepage.putExtra("userID", userID)
            startActivity(goToHomepage)
        }
        val favourite = findViewById<Button>(R.id.favourite_button)
        favourite.setOnClickListener {
            FavButton = FavButton != true
            val toastMessage = FavButton.toString()
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }
        val note_name = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.note_name)

        val fromDate = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.from)
        fromDate.transformIntoDatePicker(this, "yyyy-MM-dd")

        val toDate = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.to)
        toDate.transformIntoDatePicker(this, "yyyy-MM-dd")

        val textNote = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.noteText)
        val saveNote = findViewById<Button>(R.id.saveBtn)
        if(noteIDexists){
            val urlGetNote = "http://10.0.2.2:8080/api/v1/notes/$noteID"
            val queueH = Volley.newRequestQueue(this)
            val stringRequest = StringRequest(Request.Method.GET, urlGetNote, { response ->

                val answer = response.toString()
                val obj1 = JSONObject(answer)

                val nameHelp = obj1["note"].toString()
                note_name.setText(nameHelp)
                val odHelp = obj1["fromDate"].toString()
                fromDate.setText(odHelp)
                val platnostHelp = obj1["toDate"].toString()
                toDate.setText(platnostHelp)
                val textHelp = obj1["description"].toString()
                textNote.setText(textHelp)
            },
                    { Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show() })
            queueH.add(stringRequest)

        }
        saveNote.setOnClickListener {
            val name = note_name.text.toString()
            val description = textNote.text.toString()
            val dateFrom = fromDate.text.toString()
            val dateTo = toDate.text.toString()

            if (name.isEmpty()){
                note_name.error = "Please enter note name"
                return@setOnClickListener
            }

            if (description.isEmpty()){
                textNote.error = "Note can not be blank"
                return@setOnClickListener
            }

            if (dateFrom.isEmpty()){
                fromDate.error = "This field can not be empty"
                return@setOnClickListener
            }

            if (dateTo.isEmpty()){
                toDate.error = "This field can not be empty"
                return@setOnClickListener
            }

            val current = LocalDate.now().format(ISO_LOCAL_DATE)

            if (current.compareTo(dateTo) > 0){
                toDate.error = "Incorrect date"
                return@setOnClickListener
            }

            if (dateFrom.compareTo(dateTo) > 0){
                fromDate.error = "Incorrect date"
                return@setOnClickListener
            }

            if (!isValidIsoDateTime(dateFrom)){
                fromDate.error = "Please use YYYY-MM-DD format"
                return@setOnClickListener
            }

            if (!isValidIsoDateTime(dateTo)){
                toDate.error = "Please use YYYY-MM-DD format"
                return@setOnClickListener
            }

            val url = "http://10.0.2.2:8080/api/v1/notes/newNote"
            val queue = Volley.newRequestQueue(this)
            val noteData = JSONObject()
            try {
                noteData.put("note", name)
                noteData.put("description", description)
                noteData.put("favourite", FavButton)
                noteData.put("fromDate", dateFrom)
                noteData.put("toDate", dateTo)
                noteData.put("user_id", userID)
                if(noteIDexists){
                    noteData.put("id", noteID)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            if(noteIDexists){
                val urlUpdate = "http://10.0.2.2:8080/api/v1/notes/$noteID"
                val jsonObjectRequest = JsonObjectRequest(Request.Method.PUT, urlUpdate, noteData, { response ->
                    System.out.println("UPDATE NOTE")
                    Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
                    val goToHomepage = Intent(this, Homepage::class.java)
                    goToHomepage.putExtra("userID", userID)
                    startActivity(goToHomepage)
                }, { error -> Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show() })
                queue.add(jsonObjectRequest)

            }else{
                val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, noteData,
                        { response ->
                            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
                            val goToHomepage = Intent(this, Homepage::class.java)
                            goToHomepage.putExtra("userID", userID)
                            startActivity(goToHomepage)
                        },
                        { error -> Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show() })
                queue.add(jsonObjectRequest)
            }
        }

        val deleteButton = findViewById<Button>(R.id.deleteNote)
        deleteButton.setOnClickListener {
            val urlDelete = "http://10.0.2.2:8080/api/v1/notes/$noteID"
            val deleteQueue = Volley.newRequestQueue(this)
            val deleteRequest = StringRequest(Request.Method.DELETE, urlDelete,
                    { response ->
                        Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
                        val goHome = Intent(this, Homepage::class.java)
                        goHome.putExtra("userID", userID)
                        startActivity(goHome)
                    },
                    {
                        Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show()
                    })
            deleteQueue.add(deleteRequest)
        }
    }
}