package com.example.novigradg15

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val userRole = intent.getStringExtra("userRole")
        val userName = intent.getStringExtra("userName")

        val welcomeMessage = findViewById<TextView>(R.id.welcomeMessage)
        welcomeMessage.text  = "Welcome $userName!"

        val roleMessage = findViewById<TextView>(R.id.roleMessage)
        roleMessage.text  = "Role: $userRole"

    }
}