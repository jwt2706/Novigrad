package com.example.novigradg15

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class EmployeeWelcomeBranchActivity : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var userInfo: DocumentReference
    private lateinit var branchInfo: DocumentReference
    private lateinit var auth: FirebaseAuth
    private lateinit var welcomeMessage: TextView
    private lateinit var roleMessage: TextView
    private lateinit var modifyBranchBtn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_welcome_branch)

        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid
        userInfo = FirebaseFirestore.getInstance().collection("users").document(userId)
        branchInfo = FirebaseFirestore.getInstance().collection("branches").document(userId)

        modifyBranchBtn = findViewById(R.id.modifyBranchBtn)

        //get user data from database
        fetchAndWriteUserData()

        //get branch data from database
        fetchAndWriteBranchData()

        modifyBranchBtn.setOnClickListener() {
            modifyBranchBtnListener()
        }

    }

    fun fetchAndWriteBranchData() { //DO NOT MAKE PRIVATE, IMMA USE THIS FOR A TEST
        branchInfo.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    val name = data?.get("name") as? String
                    val address = data?.get("address") as? String
                    val telephone = data?.get("telephone") as? String
                    val timeSlots = data?.get("timeSlots") as? List<String>

                    // DO WHAT YOU WANT WITH THE DATA HERE
                    // (btw all the time slots are just in an array so you can make a for loop or something)

                } else {
                    Toast.makeText(this, "Branch data not found.", Toast.LENGTH_LONG).show()
                }
            }
    }
    private fun modifyBranchBtnListener() {
        startActivity(Intent(this,EmployeeModifyBranchActivity::class.java))
        finish()
    }
    private fun fetchAndWriteUserData() {
        userInfo.get()
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
