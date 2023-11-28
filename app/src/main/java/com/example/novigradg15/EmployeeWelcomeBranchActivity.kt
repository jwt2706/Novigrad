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
    private lateinit var branchInfo: CollectionReference
    private lateinit var auth: FirebaseAuth
    private lateinit var welcomeMessage: TextView
    private lateinit var roleMessage: TextView
    private lateinit var modifyBranchBtn: MaterialButton
    private lateinit var branchName: TextView
    private lateinit var branchAddress: TextView
    private lateinit var branchTelephone: TextView
    private lateinit var branchServices: TextView
    private lateinit var mondayHours: TextView
    private lateinit var tuesdayHours: TextView
    private lateinit var wednesdayHours: TextView
    private lateinit var thursdayHours: TextView
    private lateinit var fridayHours: TextView
    private lateinit var saturdayHours: TextView
    private lateinit var sundayHours: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_welcome_branch)

        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid
        userInfo = FirebaseFirestore.getInstance().collection("users").document(userId)
        branchInfo = FirebaseFirestore.getInstance().collection("branches")

        modifyBranchBtn = findViewById(R.id.modifyBranchBtn)

        branchName = findViewById(R.id.branchName)
        branchAddress = findViewById(R.id.branchAddress)
        branchTelephone = findViewById(R.id.branchTelephone)
        branchServices = findViewById(R.id.branchServices)
        mondayHours = findViewById(R.id.mondayHours)
        tuesdayHours = findViewById(R.id.tuesdayHours)
        wednesdayHours = findViewById(R.id.wednesdayHours)
        thursdayHours = findViewById(R.id.thursdayHours)
        fridayHours = findViewById(R.id.fridayHours)
        saturdayHours = findViewById(R.id.saturdayHours)
        sundayHours = findViewById(R.id.sundayHours)

        //get user data from database
        fetchAndWriteUserData()

        //get branch data from database
        fetchAndWriteBranchData(userId)

        modifyBranchBtn.setOnClickListener() {
            modifyBranchBtnListener()
        }

    }

    fun fetchAndWriteBranchData(userId: String):Boolean { //DO NOT MAKE PRIVATE, IMMA USE THIS FOR A TEST
        var documentFound = false
        branchInfo.document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    var documentFound = true
                    val data = documentSnapshot.data
                    val name = data?.get("name") as? String
                    val address = data?.get("address") as? String
                    val telephone = data?.get("telephone") as? String
                    val timeSlots = data?.get("timeSlots") as? List<String>
                    val services = data?.get("services") as? List<String>

                    branchName.text = "Name: $name"
                    branchAddress.text = "Address: $address"

                    if (telephone != null) {
                        branchTelephone.text = "${telephone.substring(0, 3)} ${telephone.substring(3, 6)} ${telephone.substring(6, 10)}"
                    }

                    if (services != null) {
                        branchServices.text = "Services: ${services.joinToString(separator = ", ")}"
                    }

                    if (timeSlots != null) {
                        mondayHours.text = "Monday: ${timeSlots[0]} - ${timeSlots[1]}"
                        tuesdayHours.text = "Tuesday: ${timeSlots[2]} - ${timeSlots[3]}"
                        wednesdayHours.text = "Wednesday: ${timeSlots[4]} - ${timeSlots[5]}"
                        thursdayHours.text = "Thursday: ${timeSlots[6]} - ${timeSlots[7]}"
                        fridayHours.text = "Friday: ${timeSlots[8]} - ${timeSlots[9]}"
                        saturdayHours.text = "Saturday: ${timeSlots[10]} - ${timeSlots[11]}"
                        sundayHours.text = "Sunday: ${timeSlots[12]} - ${timeSlots[13]}"
                    }


                } else {
                    Toast.makeText(this, "Branch data not found.", Toast.LENGTH_LONG).show()
                }
            }
        return documentFound
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
                    Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }
}
