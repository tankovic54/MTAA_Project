package com.fiit.notes

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.*
import android.widget.LinearLayout.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_calendar.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Calendar : AppCompatActivity(){
    var datumDo =  ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        val userID = bundle!!.getString("userID")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val backButton = findViewById<Button>(R.id.backbutton_calendar)
        backButton.setOnClickListener {
            val goHome = Intent(this, Homepage::class.java)
            goHome.putExtra("userID", userID)
            startActivity(goHome)
        }

        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        //getnutie poznamok
        val url = "http://10.0.2.2:8080/api/v1/notes/user/$userID"
        val queue = Volley.newRequestQueue(this)
        var poznamky = JSONArray()
        var emptyList = false

        var pocetPoznamok = 0
        val stringRequest = StringRequest(Request.Method.GET, url,
                { response ->
                    val answer = response.toString()
                    val obj = JSONArray(answer)
                    poznamky = JSONArray(answer)
                    pocetPoznamok = obj.length() - 1
                    if (poznamky.length() > 0) {
                        for (index: Int in 0..pocetPoznamok) {
                            val obj1 = JSONObject(obj[index].toString())
                            datumDo.add(obj1["toDate"].toString())
                        }
                    } else {
                        emptyList = true
                    }
                },
                { Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show() })

        queue.add(stringRequest)

        if(emptyList == false){


        //Thread.sleep(4_000)
        val layOutll = findViewById<LinearLayout>(R.id.ll_main_layout)
        layOutll.removeAllViews()
        //val sdf = SimpleDateFormat("dd-MM-yyyy")
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var nowDate = sdf.format(Date(calendarView.getDate()))
            var index: Int
            for (index: Int in 0..datumDo.count()-1){
            Log.d("index", "index = " + index + " pocetPoznamok: $pocetPoznamok")
            Log.d("datum", "poznamka datum = " + datumDo[index] + " upraveny: $nowDate")
            if(datumDo[index] == nowDate){
                val obj1 = JSONObject(poznamky[index].toString())
                val dynamicName = TextView(this)
                dynamicName.textSize = 40f
                dynamicName.setTextColor(Color.parseColor("#ad8f0a"))
                dynamicName.setText(obj1["note"].toString())
                val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                params.setMargins(10, 50, 10, 0) //setMargins(left, top, right, bottom);

                dynamicName.setLayoutParams(params)


                val dynamicDate = TextView(this)
                dynamicDate.textSize = 20f
                val dateText =  "From: "+obj1["fromDate"].toString()+ " To: "+obj1["toDate"].toString()
                dynamicDate.setText(dateText)

                val dynamicText = TextView(this)
                //dynamicText.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
                dynamicText.textSize = 20f
                val noteTextt = "Note text: " + obj1["description"].toString()
                dynamicText.setText(noteTextt)

                layOutll.addView(dynamicName)
                layOutll.addView(dynamicDate)
                layOutll.addView(dynamicText)

            }
        }


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
            //var noteName = findViewById<TextView>(R.id.note_name)

            layOutll.removeAllViews()
            /*var date = findViewById<TextView>(R.id.date)
            var noteText = findViewById<TextView>(R.id.note_text)*/
            var vypis = false
            //var pocetPoznamok = 0

            index = 0
            Log.d("index", "index = " + index)
            for (index: Int in 0..pocetPoznamok){
                Log.d("index", "index = " + index + " pocetPoznamok: $pocetPoznamok")
                Log.d("datum", "poznamka datum = " + datumDo[index] + " upraveny: $upravenyDat")
                if(datumDo[index] == upravenyDat){
                    vypis = true

                    //***********************************************

                    val obj1 = JSONObject(poznamky[index].toString())
                    val dynamicName = TextView(this)
                    dynamicName.textSize = 40f
                    dynamicName.setTextColor(Color.parseColor("#ad8f0a"))
                    dynamicName.setText(obj1["note"].toString())
                    val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                    params.setMargins(10, 50, 10, 0) //setMargins(left, top, right, bottom);

                    dynamicName.setLayoutParams(params)


                    val dynamicDate = TextView(this)
                    dynamicDate.textSize = 20f
                    val dateText =  "From: "+obj1["fromDate"].toString()+ " To: "+obj1["toDate"].toString()
                    dynamicDate.setText(dateText)

                    val dynamicText = TextView(this)
                    //dynamicText.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
                    dynamicText.textSize = 20f
                    val noteTextt = "Note text: " + obj1["description"].toString()
                    dynamicText.setText(noteTextt)

                    //dynamicText.setPadding(20, 20, 20, 20) // in pixels (left, top, right, bottom)

                    // add TextView to LinearLayout
                    /*val textView1 = TextView(this)
                    textView1.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT)
                    textView1.text = "programmatically created TextView1"
                    textView1.setBackgroundColor(-0x99009a) */// hex color 0xAARRGGBB

                    //textView1.setPadding(20, 20, 20, 20) // in pixels (left, top, right, bottom)

                    //linearLayout.addView(textView1)


                    layOutll.addView(dynamicName)
                    layOutll.addView(dynamicDate)
                    layOutll.addView(dynamicText)

                }
            }
        }

        }

    }
}