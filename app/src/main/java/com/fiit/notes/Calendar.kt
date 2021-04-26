package com.fiit.notes

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class Calendar : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        val userID = bundle!!.getString("userID")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        //getnutie poznamok
        val url = "http://10.0.2.2:8080/api/v1/notes/user/$userID"
        val queue = Volley.newRequestQueue(this)
        var poznamky = JSONArray()
        var emptyList = false
        var datumDo =  ArrayList<String>()
        var pocetPoznamok = 0
        val stringRequest = StringRequest(Request.Method.GET, url,
                { response ->
                    val answer = response.toString()
                    val obj = JSONArray(answer)
                    poznamky = JSONArray(answer)
                    pocetPoznamok = obj.length() - 1
                    if (poznamky.length() > 0){
                        for (index: Int in 0..pocetPoznamok){
                            val obj1 = JSONObject(obj[index].toString())
                            datumDo.add(obj1["toDate"].toString())
                        }
                    }
                    else{
                        emptyList = true
                    }
                },
                { Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show() })

        queue.add(stringRequest)

        var nowDate = calendarView.date
        Log.d("datum", "selected date:$nowDate")

        calendarView.setOnDateChangeListener{ calendarView, rok, mesiac, den ->
            Toast.makeText(this@Calendar, "selected date: $den-$mesiac-$rok", Toast.LENGTH_LONG).show()
            val mesiacu = mesiac+1
            //den mesiac rok potrebujem

            var mesiacuu  = "$mesiacu"
            if(mesiacu < 10){
                mesiacuu = "0$mesiacu"
            }else{
                mesiacuu = "$mesiacu"
            }
            var upravenyDat = "$den-"+mesiacuu+"-$rok"
            Log.d("datum", "upraveny datum: $upravenyDat")


            /*[
    {
        "id": 1,
        "user_id": 1,
        "note": "noteName1",
        "description": "descriptionOfNote",
        "fromDate": "from1",
        "toDate": "to1",
        "favourite": true
    },
    {
        "id": 3,
        "user_id": 1,
        "note": "NoteName3",
        "description": "descriptionNote3",
        "fromDate": "from2",
        "toDate": "to2",
        "favourite": false
    },
    {
        "id": 4,
        "user_id": 1,
        "note": "toto je novy note 9",
        "description": "toto je note na test",
        "fromDate": "20-01-2020",
        "toDate": "29-04-2021",
        "favourite": true
    }
]*/
            var noteName = findViewById<TextView>(R.id.note_name)
            var date = findViewById<TextView>(R.id.date)
            var noteText = findViewById<TextView>(R.id.note_text)
            var vypis = false
            var cisloVypisu = 0
            for (index: Int in 0..pocetPoznamok){
                if(datumDo[index] == upravenyDat){
                    vypis = true
                    cisloVypisu = index
                    break
                }
            }
            if(vypis == true){
                val obj1 = JSONObject(poznamky[cisloVypisu].toString())
                noteName.setText(obj1["note"].toString())
                date.setText("From: "+obj1["fromDate"]+ " To: "+obj1["toDate"])
                noteText.setText("Note text: " + obj1["description"])
            }else{
                noteName.setText("Note name")
                date.setText("From: To: ")
                noteText.setText("Note text: ")
            }

        }

    }
}