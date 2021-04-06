package com.fiit.notes

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.springframework.http.*
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate


class Login : AppCompatActivity() {

    lateinit var name: com.google.android.material.textfield.TextInputEditText
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
        name = findViewById(R.id.email_input)
        pass = findViewById(R.id.password_input)
        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener(){
            val username = name.text.toString()
            val password = pass.text.toString()
            if (username.isEmpty()){
                name.error = "Please enter username"
                return@setOnClickListener
            }

            if (password.isEmpty()){
                pass.error = "Please enter password"
                return@setOnClickListener
            }
            val url = "http://localhost:8080/api/v1/users/login"
            val authHeader: HttpAuthentication = HttpBasicAuthentication(username, password)
            val requestHeaders = HttpHeaders()
            requestHeaders.setAuthorization(authHeader)
            val requestEntity: HttpEntity<*> = HttpEntity<Any>(requestHeaders)
            val restTemplate = RestTemplate()
            try {
                // Make the HTTP GET request to the Basic Auth protected URL
                val response: ResponseEntity<String>? = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String::class.java)
                Toast.makeText(this, "Welcome in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } catch (e: HttpClientErrorException) {
                Toast.makeText(this, "Unauthorized access", Toast.LENGTH_SHORT).show()
                // Handle 401 Unauthorized response
            }
        }
    }
}