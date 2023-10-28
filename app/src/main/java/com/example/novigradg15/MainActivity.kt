package com.example.novigradg15

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
            }
        }

        signupBtn.setOnClickListener {
            val signupIntent = Intent(this, SignupActivity::class.java)
            startActivity(signupIntent)
        }

    }
}

