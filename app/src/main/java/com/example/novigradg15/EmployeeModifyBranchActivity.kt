package com.example.novigradg15

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

import com.google.firebase.firestore.QuerySnapshot





class EmployeeModifyBranchActivity : AppCompatActivity() {
    private lateinit var multiSelectBranchServices: MultiAutoCompleteTextView
    private lateinit var editBranchAddress: EditText
    private lateinit var editBranchTelephone: EditText
    private lateinit var mondayToHours: EditText
    private lateinit var mondayFromHours: EditText
    private lateinit var tuesdayToHours: EditText
    private lateinit var tuesdayFromHours: EditText
    private lateinit var wednesdayToHours: EditText
    private lateinit var wednesdayFromHours: EditText
    private lateinit var thursdayToHours: EditText
    private lateinit var thursdayFromHours: EditText
    private lateinit var fridayToHours: EditText
    private lateinit var fridayFromHours: EditText
    private lateinit var saturdayToHours: EditText
    private lateinit var saturdayFromHours: EditText
    private lateinit var sundayToHours: EditText
    private lateinit var sundayFromHours: EditText
    private lateinit var modifyBtn: MaterialButton

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_modify_branch)

        auth = FirebaseAuth.getInstance()

        mondayToHours = findViewById(R.id.mondayToHours)
        mondayFromHours = findViewById(R.id.mondayFromHours)
        tuesdayToHours = findViewById(R.id.tuesdayToHours)
        tuesdayFromHours = findViewById(R.id.tuesdayFromHours)
        wednesdayToHours = findViewById(R.id.wednesdayToHours)
        wednesdayFromHours = findViewById(R.id.wednesdayFromHours)
        thursdayToHours = findViewById(R.id.thursdayToHours)
        thursdayFromHours = findViewById(R.id.thursdayFromHours)
        fridayToHours = findViewById(R.id.fridayToHours)
        fridayFromHours = findViewById(R.id.fridayFromHours)
        saturdayToHours = findViewById(R.id.saturdayToHours)
        saturdayFromHours = findViewById(R.id.saturdayFromHours)
        sundayToHours = findViewById(R.id.sundayToHours)
        sundayFromHours = findViewById(R.id.sundayFromHours)

        modifyBtn = findViewById(R.id.saveChangesBtn)

        modifyBtn.setOnClickListener {
            val phoneNumber = editBranchTelephone.text.toString()
            // Regex pattern for phone number validation
            val pattern = Regex("^\\+[1-9]\\d{1,14}\$")
            if (pattern.matches(phoneNumber)) {
                // Phone number is valid
                saveModifyChanges()
            } else {
                // Phone number is not valid
                Toast.makeText(this, "Phone number is not valid.", Toast.LENGTH_SHORT).show()
            }
        }

        val db = FirebaseFirestore.getInstance().collection("branches")
        val services = ArrayList<String>()
        //list of the services fetched from the DB
        db.get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot) {
                    val name = document.getString("name")
                    name?.let {
                        services.add(it)
                    }
                }

                val timeEditTexts = arrayOf(mondayToHours, mondayFromHours, tuesdayFromHours, tuesdayToHours, wednesdayFromHours, wednesdayToHours, thursdayFromHours, thursdayToHours, fridayFromHours, fridayToHours, saturdayFromHours, saturdayToHours, sundayFromHours, sundayToHours)
                //Adding RegEx to Hours
                for (timeEdit in timeEditTexts) {
                    timeEdit.addTextChangedListener(object : TextWatcher {
                        //These two are just needed to satisfy the TextWatcher interface
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                        override fun afterTextChanged(s: Editable) {
                            val time = s.toString()
                            if (time.matches(Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]\$")).not()) {
                                timeEdit.error = "Invalid time format. Use HH:MM."
                            }
                        }
                    })
                }

                // Branch services logic start
                multiSelectBranchServices = findViewById(R.id.multiSelectBranchServices)
                val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, services)
                multiSelectBranchServices.setAdapter(adapter)
                multiSelectBranchServices.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
                val selectedItems = mutableSetOf<String>()
                multiSelectBranchServices.setOnItemClickListener { _, _, position, _ ->
                    val selected = adapter.getItem(position) ?: return@setOnItemClickListener

                    if (selectedItems.contains(selected)) {
                        selectedItems.remove(selected)
                    } else {
                        selectedItems.add(selected)
                    }
                    multiSelectBranchServices.setText(selectedItems.joinToString(", "))
                    multiSelectBranchServices.setSelection(multiSelectBranchServices.text.length)
                }
                multiSelectBranchServices.setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        (v as? MultiAutoCompleteTextView)?.showDropDown()
                    }
                }
                multiSelectBranchServices.setOnClickListener {
                    (it as? MultiAutoCompleteTextView)?.showDropDown()
                }
                multiSelectBranchServices.keyListener = null
                // Branch services logic end
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
                // Handle any errors that occur while retrieving the documents
            }

    }

    private fun saveModifyChanges() {
        val address = editBranchAddress.text.toString()
        val telephone = editBranchTelephone.text.toString()
        val selectedServices = multiSelectBranchServices.text.toString().split(",").map { it.trim() }
        val mondayToHoursText = mondayToHours.text.toString()
        val mondayFromHoursText = mondayFromHours.text.toString()
        val tuesdayToHoursText = tuesdayToHours.text.toString()
        val tuesdayFromHoursText = tuesdayFromHours.text.toString()
        val wednesdayToHoursText = wednesdayToHours.text.toString()
        val wednesdayFromHoursText = wednesdayFromHours.text.toString()
        val thursdayToHoursText = thursdayToHours.text.toString()
        val thursdayFromHoursText = thursdayFromHours.text.toString()
        val fridayToHoursText = fridayToHours.text.toString()
        val fridayFromHoursText = fridayFromHours.text.toString()
        val saturdayToHoursText = saturdayToHours.text.toString()
        val saturdayFromHoursText = saturdayFromHours.text.toString()
        val sundayToHoursText = sundayToHours.text.toString()
        val sundayFromHoursText = sundayFromHours.text.toString()

        val userId = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance().collection("branches")

        val timeSlots = arrayOf(mondayToHoursText, mondayFromHoursText, tuesdayFromHoursText, tuesdayToHoursText, wednesdayFromHoursText, wednesdayToHoursText, thursdayFromHoursText, thursdayToHoursText, fridayFromHoursText, fridayToHoursText, saturdayFromHoursText, saturdayToHoursText, sundayFromHoursText, sundayToHoursText)

        val data = hashMapOf(
            "name" to selectedServices,
            "address" to address,
            "telephone" to telephone,
            "timeSlots" to timeSlots,
        )

        db.document(userId!!)
            .set(data)
            .addOnCompleteListener {saveData ->
                if (saveData.isSuccessful) {
                    Toast.makeText(this, "Branch data updated.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,EmployeeWelcomeBranchActivity::class.java))
                    finish()

                } else {
                    Toast.makeText(this, "Save failed. Please try again.", Toast.LENGTH_LONG).show()
                }
            }
    }
}