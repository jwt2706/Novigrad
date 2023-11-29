package com.example.novigradg15

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    private lateinit var multiSelectBranchServices: MultiAutoCompleteTextView
    private lateinit var dayOfTheWeek: Spinner
//    private lateinit var timeEdit: EditText
    private lateinit var saveBtn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_filters)

        val db = FirebaseFirestore.getInstance().collection("services")
        var availableServices = ArrayList<String>()

        branchAddress = findViewById(R.id.editBranchAddress)
        branchTelephone = findViewById(R.id.editBranchTelephone)
//        dayOfTheWeek = findViewById(R.id.daySpinner)
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

                //Adding RegEx to Hours
//                timeEdit = findViewById(R.id.time)
//                timeEdit.addTextChangedListener(object : TextWatcher {
//                        //These two are just needed to satisfy the TextWatcher interface
//                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//                        override fun afterTextChanged(s: Editable) {
//                            val time = s.toString()
//                            if (time.matches(Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]\$")).not()) {
//                                timeEdit.error = "Invalid time format. Use HH:MM."
//                            }
//                        }
//                    })

                // Branch services logic start
                multiSelectBranchServices = findViewById(R.id.multiSelectBranchServices)
                val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, availableServices)
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
            }
    }

    private fun addFilters() {
        val intent = Intent(this, ClientWelcomeActivity::class.java)
        val services = multiSelectBranchServices.text.toString().split(",").map { it.trim() }.toCollection(ArrayList())
        intent.putExtra("branchAddress", branchAddress.text.toString())
        intent.putExtra("branchTelephone", branchTelephone.text.toString())
        intent.putExtra("services", services)
//        intent.putExtra("dayOfTheWeek", dayOfTheWeek.selectedItem.toString())
//        intent.putExtra("time", timeEdit.text.toString())
        startActivity(intent);
        finish()
    }
}