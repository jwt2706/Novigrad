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

class AddServiceActivity : AppCompatActivity() {
    private lateinit var serviceName: EditText
    private lateinit var additionalInfo: EditText
    private lateinit var documentsCheckBox: CheckBox
    private lateinit var formCheckBox: CheckBox
    private lateinit var statusCheckBox: CheckBox
    private lateinit var photoCheckBox: CheckBox
    private lateinit var addServiceBtn: MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service)

        serviceName = findViewById(R.id.serviceNameInput)
        additionalInfo = findViewById(R.id.additionalInfoInput)
        documentsCheckBox = findViewById(R.id.checkBoxDocuments)
        formCheckBox = findViewById(R.id.checkBoxForm)
        statusCheckBox = findViewById(R.id.checkBoxStatus)
        photoCheckBox = findViewById(R.id.checkBoxPhoto)
        addServiceBtn = findViewById(R.id.addServiceBtn)

        addServiceBtn.setOnClickListener {
            if (serviceName.text.toString()!="") {
                addServiceBtnListener()
            } else {
                Toast.makeText(this, "Service must have a name!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addServiceBtnListener() {

        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance().collection("services")

        if (userId != null) {
            createService(userId)
        }
    }

    fun createService(userId: String):Boolean {
        var success = false
        val db = FirebaseFirestore.getInstance().collection("services")

        val service = hashMapOf(
            "userIDofCreator" to userId,
            "additionalInfo" to additionalInfo.text.toString(),
            "documents" to documentsCheckBox.isChecked,
            "form" to formCheckBox.isChecked,
            "status" to statusCheckBox.isChecked,
            "photo" to photoCheckBox.isChecked,
        )

        db.document(serviceName.text.toString())
            .set(service)
            .addOnCompleteListener {saveData ->
                if (saveData.isSuccessful) {
                    success = true
                    Toast.makeText(this, "Service creation successful.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,ServiceSettingsActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Service creation failed. Please try again.", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_LONG).show()
            }
        return success
    }

}