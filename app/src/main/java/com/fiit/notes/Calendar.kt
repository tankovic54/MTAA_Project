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


class Calendar : AppCompatActivity() {
    var datumDo = ArrayList<String>()
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
        var index: Int
        val layOutll = findViewById<LinearLayout>(R.id.ll_main_layout)
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

                    if (emptyList == false) {
                        layOutll.removeAllViews()
                        //val sdf = SimpleDateFormat("dd-MM-yyyy")
                        val sdf = SimpleDateFormat("yyyy-MM-dd")
                        var nowDate = sdf.format(Date(calendarView.getDate()))

                        for (index: Int in 0..pocetPoznamok) { //0..datumDo.count()-1
                            Log.d("index", "index = " + index + " pocetPoznamok: $pocetPoznamok")
                            Log.d("datum", "poznamka datum = " + datumDo[index] + " upraveny: $nowDate")
                            if (datumDo[index] == nowDate) {
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
                                val dateText = "From: " + obj1["fromDate"].toString() + " To: " + obj1["toDate"].toString()
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
                    }

                },
                { Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show() })

        queue.add(stringRequest)

        if (emptyList == false) {

            calendarView.setOnDateChangeListener { calendarView, rok, mesiac, den ->
                val mesiacu = mesiac + 1
                //rok mesiac den potrebujem

                var mesiacuu = "$mesiacu"
                if (mesiacu < 10) {
                    mesiacuu = "0$mesiacu"
                } else {
                    mesiacuu = "$mesiacu"
                }
                var upravenyDat = "$rok-" + mesiacuu + "-$den"
                Log.d("datum", "upraveny datum: $upravenyDat")


                layOutll.removeAllViews()
                var vypis = false
                //var pocetPoznamok = 0
                index = 0
                Log.d("index", "index = " + index)
                for (index: Int in 0..pocetPoznamok) {
                    Log.d("index", "index = " + index + " pocetPoznamok: $pocetPoznamok")
                    Log.d("datum", "poznamka datum = " + datumDo[index] + " upraveny: $upravenyDat")
                    if (datumDo[index] == upravenyDat) {
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
                        val dateText = "From: " + obj1["fromDate"].toString() + " To: " + obj1["toDate"].toString()
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
            }

        }

    }
}