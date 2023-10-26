package com.example.novigradg15

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.material.button.MaterialButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val username = findViewById<EditText>(R.id.usernameInput);
        val password = findViewById<EditText>(R.id.passwordInput);

        val loginBtn = findViewById<MaterialButton>(R.id.loginbtn );

        loginBtn.setOnClickListener {
            if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                //correct
            } else {
                Toast.makeText(this, "Incorrect username/password", Toast.LENGTH_SHORT).show()
                //incorrect
            }
        }

    }
}

