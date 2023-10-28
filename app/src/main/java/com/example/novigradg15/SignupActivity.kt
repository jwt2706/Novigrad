package com.example.novigradg15

<<<<<<< HEAD
import android.content.Intent
=======
>>>>>>> main
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
<<<<<<< HEAD
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
=======
import androidx.compose.material3.lightColorScheme
import com.google.android.material.button.MaterialButton
>>>>>>> main

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
<<<<<<< HEAD
        val auth = FirebaseAuth.getInstance()
=======
>>>>>>> main

        val signupBtn = findViewById<MaterialButton>(R.id.signupbtn)

        signupBtn.setOnClickListener {
            val userName: String = findViewById<EditText>(R.id.usernameInput).getText().toString()
            val email: String = findViewById<EditText>(R.id.emailInput).getText().toString()
            val password: String = findViewById<EditText>(R.id.passwordInput).getText().toString()
            val licenseLevel: String = findViewById<Spinner>(R.id.driverLicenseSpinner).selectedItem.toString()
            val role: String = findViewById<Spinner>(R.id.roleSpinner).selectedItem.toString()

<<<<<<< HEAD
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if(task.isSuccessful) {

                    val userId = auth.currentUser?.uid
                    val db = FirebaseFirestore.getInstance().collection("users")

                    val user = hashMapOf(
                        "userName" to userName,
                        "email" to email,
                        "role" to role,
                        "licenseLevel" to licenseLevel,
                    )
                    db.document(userId!!)
                        .set(user)
                        .addOnCompleteListener {saveData ->
                            if (saveData.isSuccessful) {

                                Toast.makeText(this, "Account creation successful.", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,MainActivity::class.java))
                                finish()

                            } else {
                                Toast.makeText(this, "EPIC FAIL LMAO", Toast.LENGTH_SHORT).show()

                            }
                        }
                }
            }.addOnFailureListener { e ->
                Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }
=======
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
>>>>>>> main
}