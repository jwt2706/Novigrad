package com.example.novigradg15

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var loginBtn: MaterialButton
    private lateinit var signupBtn: MaterialButton
    private lateinit var email: String
    private lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        email = findViewById<EditText>(R.id.usernameInput).getText().toString()
        password = findViewById<EditText>(R.id.passwordInput).getText().toString()

        loginBtn = findViewById(R.id.loginbtn)
        signupBtn = findViewById(R.id.signupbtn)

        auth = FirebaseAuth.getInstance()
	
	    //attempts login by verifying the submitted credentials on firebase
        loginBtn.setOnClickListener {
            loginBtnListener()
        }

        signupBtn.setOnClickListener {
            signupBtnListener()
        }

    }

    private fun loginBtnListener() {
        //checks for empty fields
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

    private fun signupBtnListener() {
        val signupIntent = Intent(this, SignupActivity::class.java)
        startActivity(signupIntent)
    }
}

