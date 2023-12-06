package com.example.novigradg15

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.MultiAutoCompleteTextView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.Normalizer.Form
import java.util.Calendar
import java.util.Locale

class PhotoVisitActivity : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var userInfo: DocumentReference
    private lateinit var branchInfo: CollectionReference
    private lateinit var auth: FirebaseAuth
    private lateinit var requestBtn: MaterialButton
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
    private lateinit var date: EditText
    private lateinit var birthdate: EditText
    private lateinit var timeEdit: EditText
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, ClientWelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_visit)

        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid
        userInfo = FirebaseFirestore.getInstance().collection("users").document(userId)
        branchInfo = FirebaseFirestore.getInstance().collection("branches")

        requestBtn = findViewById(R.id.requestBtn)

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

        intent.getStringExtra("branchName")?.let { getBranchByName(it) }
        intent.getStringExtra("selectedService")?.let { branchServices.text = "Service: $it" }

        val db = FirebaseFirestore.getInstance().collection("services")
        var availableServices = ArrayList<String>()
        //list of the services fetched from the DB
        db.get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot) {
                    availableServices.add(document.reference.id)
                }

                date = findViewById(R.id.date)
                date.setOnClickListener {
                    showDatePicker()
                }

                //Adding RegEx to Time
                timeEdit = findViewById(R.id.time)
                timeEdit.addTextChangedListener(object : TextWatcher {
                    //These two are just needed to satisfy the TextWatcher interface
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        val time = s.toString()
                        if (time.matches(Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]\$")).not()) {
                            timeEdit.error = "Invalid time format. Use HH:MM."
                        }
                    }
                })
            }

        birthdate = findViewById(R.id.dateOfBirth)
        birthdate.setOnClickListener {
            showBirthdatePicker()
        }

        // Prepares activity that shows image upload options

        var uploadPersonalImageBtn = findViewById<MaterialButton>(R.id.uploadPersonalImageButton)
        uploadPersonalImageBtn.setOnClickListener {
            pickImageLauncher.launch(
                Intent(MediaStore.ACTION_PICK_IMAGES)
            )
        }

        var uploadAddressImageBtn = findViewById<MaterialButton>(R.id.uploadAddressImageButton)
        uploadAddressImageBtn.setOnClickListener {
            pickImageLauncher.launch(
                Intent(MediaStore.ACTION_PICK_IMAGES)
            )
        }

        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode != Activity.RESULT_OK) {
                    Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show()
                } else {
                    var imagePreview = findViewById<ImageView>(R.id.imagePreview)
                    val uri = it.data?.data
                    loadImageFromUri(this, imagePreview, uri)
                }
            }

        requestBtn.setOnClickListener {
            requestBtnListener()
        }

    }

    // Allows loading an image in an ImageView from a Uri
    private fun loadImageFromUri(context: Context, image: ImageView, uri: Uri?) {
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                image.setImageBitmap(bitmap)
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    public fun getBranchByName(nameToFind: String): Boolean {
        var success = false
        val db = FirebaseFirestore.getInstance().collection("branches")
        db.get()
            .addOnSuccessListener { snapshot ->
                success = true
                for (document in snapshot) {
                    val data = document.data
                    val name = data?.get("name") as? String
                    if (!name.isNullOrBlank() && name.lowercase() == nameToFind.lowercase()) {
                        branchName.text = "Branch name: $name"
                        branchAddress.text = "Address: ${data?.get("address") as? String}"
                        branchTelephone.text = "Telephone: ${data?.get("telephone") as? String}"

                        var timeSlots = data?.get("timeSlots") as? List<String>
                        if (!timeSlots.isNullOrEmpty()) {
                            mondayHours.text = "Monday: ${timeSlots[0]} - ${timeSlots[1]}"
                            tuesdayHours.text = "Tuesday: ${timeSlots[2]} - ${timeSlots[3]}"
                            wednesdayHours.text = "Wednesday: ${timeSlots[4]} - ${timeSlots[5]}"
                            thursdayHours.text = "Thursday: ${timeSlots[6]} - ${timeSlots[7]}"
                            fridayHours.text = "Friday: ${timeSlots[8]} - ${timeSlots[9]}"
                            saturdayHours.text = "Saturday: ${timeSlots[10]} - ${timeSlots[11]}"
                            sundayHours.text = "Sunday: ${timeSlots[12]} - ${timeSlots[13]}"
                        }
                    }
                }
            }
        return success
    }

    private fun requestBtnListener() {
        val userId = auth.currentUser?.uid
        val requestsDB = FirebaseFirestore.getInstance().collection("requests")

        val data = hashMapOf(
            "branchName" to intent.getStringExtra("branchName"),
            "customerName" to findViewById<EditText>(R.id.fullName).text.toString(),
            "date" to date.text.toString(),
            "selectedService" to intent.getStringExtra("selectedService"),
            "timeSelected" to timeEdit.text.toString(),
            "email" to findViewById<EditText>(R.id.email).text.toString(),
            "dateOfBirth" to findViewById<EditText>(R.id.dateOfBirth).text.toString(),
            "address" to findViewById<EditText>(R.id.address).text.toString(),
            "license" to "N/A",
        )

        requestsDB.document(userId!!)
            .set(data)
            .addOnCompleteListener { saveData ->
                if (saveData.isSuccessful) {
                    Toast.makeText(this, "Data saved.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ClientWelcomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Data save failed. Please try again.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_LONG).show()
            }

        val intent = Intent(this, ClientWelcomeActivity::class.java)
        intent.putExtra("branchAddress", "")
        intent.putExtra("branchTelephone", "")
        intent.putExtra("services", ArrayList<String>())
        intent.putExtra("dayOfTheWeek", "")
        intent.putExtra("time", "")
        startActivity(intent)
        finish()
    }

    private fun showDatePicker() {
        // Get current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Date picker dialog
        val datePickerDialog = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                if (selectedDate.after(calendar)) {
                    // Date is in the future
                    date.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay))
                } else {
                    // Date is not in the future, show error
                    Toast.makeText(this, "Please select a future date", Toast.LENGTH_SHORT).show()
                }
            }, year, month, day)

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun showBirthdatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                if (selectedDate.before(calendar)) {
                    birthdate.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay))
                } else {
                    Toast.makeText(this, "Please select a past date", Toast.LENGTH_SHORT).show()
                }
            }, year, month, day)

        calendar.add(Calendar.DAY_OF_MONTH, -1)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        datePickerDialog.show()
    }
}