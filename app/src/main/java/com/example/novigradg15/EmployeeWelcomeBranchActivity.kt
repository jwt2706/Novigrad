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

class EmployeeWelcomeBranchActivity : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var db: DocumentReference
    private lateinit var auth: FirebaseAuth
    private lateinit var welcomeMessage: TextView
    private lateinit var roleMessage: TextView
    private lateinit var modifyBranchBtn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_welcome_branch)

        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid
        db = FirebaseFirestore.getInstance().collection("users").document(userId)

        modifyBranchBtn = findViewById(R.id.modifyBranchBtn)

        //get user data from database
        fetchAndWriteUserData()

        modifyBranchBtn.setOnClickListener() {
            modifyBranchBtnListener()
        }

    }

    private fun modifyBranchBtnListener() {
        startActivity(Intent(this,EmployeeModifyBranchActivity::class.java))
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