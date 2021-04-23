package com.fiit.notes

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class Login : AppCompatActivity() {

    lateinit var email: com.google.android.material.textfield.TextInputEditText
    lateinit var pass: com.google.android.material.textfield.TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val regButton = findViewById<Button>(R.id.registration_button)
        regButton.setOnClickListener{
            val goToReg = Intent(this, RegisterClass:: class.java)
            startActivity(goToReg)
        }
        email = findViewById(R.id.email_input)
        pass = findViewById(R.id.password_input)
        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener(){
            val mail = email.text.toString()
            val password = pass.text.toString()
            if (mail.isEmpty()){
                email.error = "Please enter mail"
                return@setOnClickListener
            }

            if (password.isEmpty()){
                pass.error = "Please enter password"
                return@setOnClickListener
            }
            val url = "http://10.0.2.2:8080/api/v1/users/login"
            val queue = Volley.newRequestQueue(this)
            val loginData = JSONObject()
            try {
                loginData.put("email",mail )
                loginData.put("password", password)
            } catch (e: JSONException) {
                e.printStackTrace()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, loginData, { response -> Toast.makeText(this, "Welcome in", Toast.LENGTH_SHORT).show()
                val response_string = response.toString()
                val userID = response_string.substring(response_string.indexOf(":") + 1, response_string.indexOf(","))
                val goHome = Intent(this, Homepage:: class.java)
                goHome.putExtra("userID",userID)
                startActivity(goHome) },
                    { error -> Toast.makeText(this, "Wrong email or password", Toast.LENGTH_SHORT).show() })

            queue.add(jsonObjectRequest)

        }
    }
}