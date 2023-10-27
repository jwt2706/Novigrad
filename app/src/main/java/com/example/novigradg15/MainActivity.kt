package com.example.novigradg15

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        val email: String = findViewById<EditText>(R.id.usernameInput).getText().toString();
        val password: String = findViewById<EditText>(R.id.passwordInput).getText().toString();

        val loginBtn = findViewById<MaterialButton>(R.id.loginbtn );
        val signupBtn = findViewById<MaterialButton>(R.id.signupbtn);

        var userRole = "none";

        loginBtn.setOnClickListener {
            if (email == "admin" && password == "admin") {
                Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show()
                userRole = "admin";

            } else if (email == "customer" && password == "customer") {
                Toast.makeText(this, "Customer Login Successful", Toast.LENGTH_SHORT).show()
                userRole = "customer";

            } else if (email == "employee" && password == "employee") {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                userRole = "employee";

            } else {
                Toast.makeText(this, "Incorrect username/password", Toast.LENGTH_SHORT).show()
            }

            if (userRole != "none") {
                val loginIntent = Intent(this, WelcomeActivity::class.java)
                loginIntent.putExtra("userRole", userRole)
                loginIntent.putExtra("userName", email)
                startActivity(loginIntent)
            }

            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Toast.makeText(this, "Account creation successful.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

        signupBtn.setOnClickListener {
            val signupIntent = Intent(this, SignupActivity::class.java)
            startActivity(signupIntent)
        }

    }
}

