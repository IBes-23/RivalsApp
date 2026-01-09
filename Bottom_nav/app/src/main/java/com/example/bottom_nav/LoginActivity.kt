package com.example.bottom_nav

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class LoginActivity : AppCompatActivity() {
    private lateinit var editTextUsername: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextUsername = findViewById(R.id.editTextTextEmailAddress)
        editTextPassword = findViewById(R.id.editTextTextpassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {

            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            Thread {
                sendGetRequest(username, password)
            }.start()
        }
    }


    private fun sendGetRequest(username: String, password: String) {
        try {
            val url = URL("http://mrivals.x10.mx/userlogin.php?username=$username&password=$password")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"
            )
            connection.setRequestProperty(
                "Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
            )
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            val responseCode = connection.responseCode
            if (responseCode == 200) {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                inputStream.close()

                runOnUiThread {
                    val responseStr = response.toString()
                    // Check if response contains user_id (Access Granted;123)
                    if (responseStr.startsWith("Access Granted")) {
                        // Response format: Access Granted;user_id;email;username;password
                        val parts = responseStr.split(";")
                        if (parts.size >= 6) {
                            UserSession.userId = parts[1]
                            UserSession.username = parts[2]
                            UserSession.uid = parts[3]
                            UserSession.email = parts[4]
                            UserSession.password = parts[5]
                        }

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.putExtra("start_fragment", "first")
                        startActivity(intent)
                        finish()  // Close LoginActivity after successful login
                    } else {
                        showDialog("Login", responseStr)
                    }
                }
            } else {
                runOnUiThread {
                    showDialog("Login", "Failed to send request. Response code: $responseCode")
                }
            }
        } catch (e: Exception) {
            runOnUiThread {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                showDialog("Login", "Error: ${e.message}")
            }
        }
    }

    private fun showDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setCancelable(false)
        builder.setPositiveButton("Close") { dialog, _ ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}
