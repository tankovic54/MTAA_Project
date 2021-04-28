package com.fiit.notes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_settings.*
import org.json.JSONException
import org.json.JSONObject

class UpdateAccount: AppCompatActivity() {
    lateinit var username: com.google.android.material.textfield.TextInputEditText
    lateinit var email: com.google.android.material.textfield.TextInputEditText
    lateinit var password: com.google.android.material.textfield.TextInputEditText
    lateinit var confirm: com.google.android.material.textfield.TextInputEditText
    lateinit var confirmButton: Button
    lateinit var backButton: Button
    lateinit var active_password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        val userID = bundle!!.getString("userID")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_account)

        username = findViewById(R.id.update_name)
        email = findViewById(R.id.update_email)
        password = findViewById(R.id.update_password)
        confirm = findViewById(R.id.confirm_password)
        confirmButton = findViewById(R.id.confirmButton)
        backButton = findViewById(R.id.backButton_pass)


        val user_info_url = "http://10.0.2.2:8080/api/v1/users/$userID"
        val getQueue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, user_info_url,
                { response ->
                    val answer = response.toString()
                    val obj = JSONObject(answer)
                    val getName = obj.getString("name")
                    val getPass = obj.getString("password")
                    val getMail = obj.getString("email")
                    username.setText(getName)
                    email.setText(getMail)
                    active_password = getPass

                },
                { Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show() })
        getQueue.add(stringRequest)


        confirmButton.setOnClickListener {
           var password_string = password.text.toString()
           val confirm_string = confirm.text.toString()
            if (password_string != confirm_string){
                confirm.error = "Passwords do not match"
                return@setOnClickListener
            }

            if (password_string.length <= 6){
                password.error = "Password has to have at least 6 characters"
                return@setOnClickListener
            }

            if (password_string.isEmpty()){
                password_string = active_password
            }

            val username_string = username.text.toString()
            if (username_string.isEmpty()){
                username.error = "This field can not be empty"
                return@setOnClickListener
            }
            val email_string = email.text.toString()
            if (email_string.isEmpty()){
                email.error = "This field can not be empty"
                return@setOnClickListener
            }

            val url = "http://10.0.2.2:8080//api/v1/users/$userID"
            val queue = Volley.newRequestQueue(this)
            val updateData = JSONObject()
            try {
                updateData.put("name", username_string)
                updateData.put("mail", email_string)
                updateData.put("password", password_string)

            }catch (e: JSONException) {
                e.printStackTrace()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
            val jsonObjectRequest = JsonObjectRequest(Request.Method.PUT,url, updateData,
                    {
                        response -> Toast.makeText(this, "Details updated", Toast.LENGTH_SHORT).show()
                    },
                    {
                        error -> Toast.makeText(this, "Incorrect details", Toast.LENGTH_SHORT).show()
                    })
            queue.add(jsonObjectRequest)

            val goBack = Intent(this, settings::class.java)
            goBack.putExtra("userID",userID)
            startActivity(goBack)
        }
        backButton.setOnClickListener {
            val goBack = Intent(this, settings::class.java)
            goBack.putExtra("userID",userID)
            startActivity(goBack)
        }

    }
}