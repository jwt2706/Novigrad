package com.example.novigradg15

import android.hardware.SensorAdditionalInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox

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
            addServiceBtnListener()
        }
    }

    private fun addServiceBtnListener() {
        val requiredDocumentsMap = mapOf(
            "documents" to documentsCheckBox.isChecked,
            "form" to formCheckBox.isChecked,
            "status" to statusCheckBox.isChecked,
            "photo" to photoCheckBox.isChecked
        )

        val Service = Service(serviceName.text.toString(), additionalInfo.text.toString(), requiredDocumentsMap)

        // ADD SERVICE OBJECT TO DATABASE HERE
    }
}

class Service(name: String, additionalInfo: String, requiredDocuments: Map<String, Boolean>)