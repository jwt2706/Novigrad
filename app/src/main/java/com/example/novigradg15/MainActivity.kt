package com.example.novigradg15

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.material.button.MaterialButton
<<<<<<< HEAD
import com.google.firebase.auth.FirebaseAuth
=======
>>>>>>> main

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
<<<<<<< HEAD
        val auth = FirebaseAuth.getInstance()
        val loginBtn = findViewById<MaterialButton>(R.id.loginbtn )
        val signupBtn = findViewById<MaterialButton>(R.id.signupbtn)

        loginBtn.setOnClickListener {
            val email: String = findViewById<EditText>(R.id.usernameInput).getText().toString()
            val password:String = findViewById<EditText>(R.id.passwordInput).getText().toString()

            if (email == "" || password == "") {
                Toast.makeText(this, "Missing credentials!", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Successful login", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,WelcomeActivity::class.java))
                        finish()
                    } else
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                }
=======

        val username = findViewById<EditText>(R.id.usernameInput);
        val password = findViewById<EditText>(R.id.passwordInput);

        val loginBtn = findViewById<MaterialButton>(R.id.loginbtn );
        val signupBtn = findViewById<MaterialButton>(R.id.signupbtn);

        var userRole = "none";

        loginBtn.setOnClickListener {
            if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
                Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show()
                userRole = "admin";

            } else if (username.getText().toString().equals("customer") && password.getText().toString().equals("customer")) {
                Toast.makeText(this, "Customer Login Successful", Toast.LENGTH_SHORT).show()
                userRole = "customer";

            } else if (username.getText().toString().equals("employee") && password.getText().toString().equals("employee")) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                userRole = "employee";

            } else {
                Toast.makeText(this, "Incorrect username/password", Toast.LENGTH_SHORT).show()
            }

            if (userRole != "none") {
                val loginIntent = Intent(this, WelcomeActivity::class.java)
                loginIntent.putExtra("userRole", userRole)
                loginIntent.putExtra("userName", username.getText().toString())
                startActivity(loginIntent)
>>>>>>> main
            }
        }

        signupBtn.setOnClickListener {
            val signupIntent = Intent(this, SignupActivity::class.java)
            startActivity(signupIntent)
        }

    }
}

