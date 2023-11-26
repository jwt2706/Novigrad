package com.example.novigradg15

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class EmployeeWelcomeNoBranchActivity : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var db: DocumentReference
    private lateinit var auth: FirebaseAuth
    private lateinit var welcomeMessage: TextView
    private lateinit var roleMessage: TextView
    private lateinit var branchSettingsBtn: MaterialButton
    private lateinit var serviceSettingsBtn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_welcome_no_branch)

        branchSettingsBtn = findViewById(R.id.branchSettingsBtn)
        serviceSettingsBtn = findViewById(R.id.serviceSettingsBtn)
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid
        db = FirebaseFirestore.getInstance().collection("users").document(userId)

        //get user data from database
        fetchAndWriteUserData()

        serviceSettingsBtn.setOnClickListener() {
            serviceSettingsBtnListener()
        }

        branchSettingsBtn.setOnClickListener() {
            branchSettingsBtnListener()
        }

    }

    private fun serviceSettingsBtnListener() {
        startActivity(Intent(this,ServiceSettingsActivity::class.java))
        finish()
    }

    private fun branchSettingsBtnListener() {
        startActivity(Intent(this,BranchSettingsActivity::class.java))
        finish()
    }

    private fun fetchAndWriteUserData() {
        db.get()
            .addOnSuccessListener {documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    val role = data?.get("role") as? String
                    val userName = data?.get("userName") as? String
                    //display data in welcome message
                    welcomeMessage = findViewById(R.id.welcomeMessage)
                    welcomeMessage.text  = "Welcome $userName!"
                    roleMessage = findViewById(R.id.roleMessage)
                    roleMessage.text  = "Role: $role"
                } else {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }



}
