package com.example.novigradg15

import android.content.Intent
import android.hardware.SensorAdditionalInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ModifyServiceActivity : AppCompatActivity() {
    private lateinit var serviceName: EditText
    private lateinit var additionalInfo: EditText
    private lateinit var documentsCheckBox: CheckBox
    private lateinit var formCheckBox: CheckBox
    private lateinit var statusCheckBox: CheckBox
    private lateinit var photoCheckBox: CheckBox
    private lateinit var modifyServiceBtn: MaterialButton

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, AdminWelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_service)

        val intent = intent
        val name = intent.getStringExtra("name")?: "Service Name"
        val documentsValue = intent.getBooleanExtra("documentsValue", false)
        val formValue = intent.getBooleanExtra("formValue", false)
        val statusValue = intent.getBooleanExtra("statusValue", false)
        val photoValue = intent.getBooleanExtra("photoValue", false)
        val additionalInfoValue = intent.getStringExtra("additionalInfoValue")?: "Enter additional information"

        serviceName = findViewById(R.id.serviceNameInput)
        additionalInfo = findViewById(R.id.additionalInfoInput)
        documentsCheckBox = findViewById(R.id.checkBoxDocuments)
        formCheckBox = findViewById(R.id.checkBoxForm)
        statusCheckBox = findViewById(R.id.checkBoxStatus)
        photoCheckBox = findViewById(R.id.checkBoxPhoto)
        modifyServiceBtn = findViewById(R.id.modifyServiceBtn)

        serviceName.setText(name)
        documentsCheckBox.isChecked =documentsValue
        formCheckBox.isChecked =formValue
        statusCheckBox.isChecked =statusValue
        photoCheckBox.isChecked =photoValue
        additionalInfo.setText(additionalInfoValue)

        modifyServiceBtn.setOnClickListener {
            if (serviceName.text.toString()!="") {
                modifyServiceBtnListener()
            } else {
                Toast.makeText(this, "Service must have a name!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Works by deleted the present service and replacing it with the modified one
    private fun modifyServiceBtnListener() {


        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance().collection("services")

        val service = hashMapOf(
            "userIDofCreator" to userId,
            "additionalInfo" to additionalInfo.text.toString(),
            "documents" to documentsCheckBox.isChecked,
            "form" to formCheckBox.isChecked,
            "status" to statusCheckBox.isChecked,
            "photo" to photoCheckBox.isChecked,
        )

        val documentReference = db.document(intent.getStringExtra("name")?: "Service Name")
        documentReference.delete()
            .addOnSuccessListener {

            }
                .addOnFailureListener { e ->
                    Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
                }

        // ADD SERVICE OBJECT TO DATABASE HERE
        db.document(serviceName.text.toString())
            .set(service)
            .addOnCompleteListener {saveData ->
                if (saveData.isSuccessful) {
                    Toast.makeText(this, "Service creation successful.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,ServiceSettingsActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Service creation failed. Please try again.", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_LONG).show()
            }

    }
}