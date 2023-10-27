package com.example.novigradg15

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.compose.material3.lightColorScheme
import com.google.android.material.button.MaterialButton

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signupBtn = findViewById<MaterialButton>(R.id.signupbtn)

        signupBtn.setOnClickListener {
            val userName: String = findViewById<EditText>(R.id.usernameInput).getText().toString()
            val email: String = findViewById<EditText>(R.id.emailInput).getText().toString()
            val password: String = findViewById<EditText>(R.id.passwordInput).getText().toString()
            val licenseLevel: String = findViewById<Spinner>(R.id.driverLicenseSpinner).selectedItem.toString()
            val role: String = findViewById<Spinner>(R.id.roleSpinner).selectedItem.toString()

            val User = User(userName, email, password, licenseLevel, role);
            Toast.makeText(this, "Account creation successful.", Toast.LENGTH_SHORT).show()
        }
    }
}

class User(
    private var userName: String,
    private var email: String,
    private var password: String,
    private var licenseLevel: String,
    private var role: String
) {
    fun getUserName(): String {
        return userName;
    }
    fun getEmail(): String {
        return email;
    }
    fun getPassword(): String {
        return password;
    }
    fun getLicenseLevel(): String {
        return licenseLevel;
    }
    fun getRole(): String {
        return role;
    }
}