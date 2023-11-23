package com.example.novigradg15

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private lateinit var db: DocumentReference
    private lateinit var loginBtn: MaterialButton
    private lateinit var signupBtn: MaterialButton
    private lateinit var email: String
    private lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginBtn = findViewById(R.id.loginbtn)
        signupBtn = findViewById(R.id.signupbtn)

        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid
        db = FirebaseFirestore.getInstance().collection("users").document(userId)
	
	    //attempts login by verifying the submitted credentials on firebase
        loginBtn.setOnClickListener {
            loginBtnListener()
        }

        signupBtn.setOnClickListener {
            signupBtnListener()
        }

    }

    private fun loginBtnListener() {
        email = findViewById<EditText>(R.id.usernameInput).getText().toString()
        password = findViewById<EditText>(R.id.passwordInput).getText().toString()
        //checks for empty fields
        if (email == "" || password == "") {
            Toast.makeText(this, "Missing credentials!", Toast.LENGTH_SHORT).show()
        } else {
            logIn(email, password)
        }
    }

    fun logIn(email: String, password: String):Boolean {
        var success = false
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successful login", Toast.LENGTH_SHORT).show()
                db.get()
                    .addOnSuccessListener { documentSnapshot ->
                        success = true
                        if (documentSnapshot.exists()) {
                            val data = documentSnapshot.data
                            val role = data?.get("role") as? String
                            if (role == "Admin") {
                                startActivity(Intent(this, AdminWelcomeActivity::class.java))
                                finish()
                            } else {
                                startActivity(Intent(this, WelcomeActivity::class.java))
                                finish()
                            }
                        } else {
                            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
            } else
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
        }
        return success
    }

    private fun signupBtnListener() {
        val signupIntent = Intent(this, SignupActivity::class.java)
        startActivity(signupIntent)
    }
}

