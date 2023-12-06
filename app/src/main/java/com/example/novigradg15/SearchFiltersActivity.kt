package com.example.novigradg15

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore


class SearchFiltersActivity : AppCompatActivity() {
    private lateinit var branchAddress: EditText
    private lateinit var branchTelephone: EditText
    private lateinit var serviceSpinner: Spinner
    private lateinit var daySpinner: Spinner
    private lateinit var timeEdit: EditText
    private lateinit var saveBtn: MaterialButton

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, ClientWelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_filters)

        val db = FirebaseFirestore.getInstance().collection("services")
        var availableServices = ArrayList<String>()

        branchAddress = findViewById(R.id.editBranchAddress)
        branchTelephone = findViewById(R.id.editBranchTelephone)
        daySpinner = findViewById(R.id.daySpinner)
        saveBtn = findViewById(R.id.saveChangesBtn)
        saveBtn.setOnClickListener {
            addFilters()
        }

        //list of the services fetched from the DB
        db.get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot) {
                    availableServices.add(document.reference.id)
                }

//                Adding RegEx to Hours
                timeEdit = findViewById(R.id.time)
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

                availableServices.add(0, "None")
                // Branch spinner adapter
                serviceSpinner = findViewById(R.id.serviceSpinner)
                val serviceAdapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    availableServices

                )
                serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                serviceSpinner.adapter = serviceAdapter

                // Day spinner adapter
                val dayAdapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,

                    arrayOf("None", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" )

                )
                dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                daySpinner.adapter = dayAdapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    private fun addFilters() {
        val intent = Intent(this, ClientWelcomeActivity::class.java)
        val service = serviceSpinner.selectedItem.toString()
        intent.putExtra("branchAddress", branchAddress.text.toString())
        intent.putExtra("branchTelephone", branchTelephone.text.toString())
        intent.putExtra("service", service)
        intent.putExtra("dayOfTheWeek", daySpinner.selectedItem.toString())
        intent.putExtra("time", timeEdit.text.toString())
        startActivity(intent);
        finish()
    }
}