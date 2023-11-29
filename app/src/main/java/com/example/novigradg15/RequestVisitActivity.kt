package com.example.novigradg15

import android.app.Activity
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

class RequestVisitActivity : AppCompatActivity() {

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
    private lateinit var timeEdit: EditText
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_visit)

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
                // Todo AHMED: Add regex to date

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

        // Prepares activity that shows image upload options

        var uploadImageBtn = findViewById<MaterialButton>(R.id.uploadImageButton)
        uploadImageBtn.setOnClickListener {
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

    private fun getBranchByName(nameToFind: String): Boolean {
        val db = FirebaseFirestore.getInstance().collection("branches")
        db.get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot) {
                    val data = document.data
                    val name = data?.get("name") as? String
                    Log.d("!!!!!", name.toString())
                    Log.d("!!!!!", nameToFind)
                    if (!name.isNullOrBlank() && name.lowercase() == nameToFind.lowercase()) {
                        Log.d("?????", name.toString())
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

        return false
    }

    private fun requestBtnListener() {
        /* Todo JACOB: Make a new database for "requested services"
        theres probably a better way to do this but oh well
         The requested services will take the values below:
         (All initialised above)
         - branchName
         - customerName (you might need to fetchAndWriteUserData)
         - date
         - selected service: multiSelectBranchServices.textToString()
         - time selected
         - all the form data (you gotta initialise this)
            - firstName, lastName, address, date of birth, license level (will be null for some)
            - im leaving out the documents (they are images and saving that will be a big mess)
         */

        // BY THE WAY this needs to copied into healthvisitactivity and photovisitactivity but i will do that dont do it

        val intent = Intent(this, ClientWelcomeActivity::class.java)
        intent.putExtra("branchAddress", "")
        intent.putExtra("branchTelephone", "")
        intent.putExtra("services", ArrayList<String>())
        intent.putExtra("dayOfTheWeek", "")
        intent.putExtra("time", "")
        startActivity(intent)
        finish()
    }

//    fun fetchAndWriteBranchData(userId: String):Boolean { //DO NOT MAKE PRIVATE, IMMA USE THIS FOR A TEST
//        var documentFound = false
//        branchInfo.document(userId).get()
//            .addOnSuccessListener { documentSnapshot ->
//                if (documentSnapshot.exists()) {
//                    var documentFound = true
//                    val data = documentSnapshot.data
//                    val name = data?.get("name") as? String
//                    val address = data?.get("address") as? String
//                    val telephone = data?.get("telephone") as? String
//                    val timeSlots = data?.get("timeSlots") as? List<String>
//                    val services = data?.get("services") as? List<String>
//
//                    branchName.text = "Name: $name"
//                    branchAddress.text = "Address: $address"
//
//                    if (telephone != null) {
//                        branchTelephone.text = "${telephone.substring(0, 3)} ${telephone.substring(3, 6)} ${telephone.substring(6, 10)}"
//                    }
//
//                    if (services != null) {
//                        branchServices.text = "Services: ${services.joinToString(separator = ", ")}"
//                    }
//
//                    if (timeSlots != null) {
//                        mondayHours.text = "Monday: ${timeSlots[0]} - ${timeSlots[1]}"
//                        tuesdayHours.text = "Tuesday: ${timeSlots[2]} - ${timeSlots[3]}"
//                        wednesdayHours.text = "Wednesday: ${timeSlots[4]} - ${timeSlots[5]}"
//                        thursdayHours.text = "Thursday: ${timeSlots[6]} - ${timeSlots[7]}"
//                        fridayHours.text = "Friday: ${timeSlots[8]} - ${timeSlots[9]}"
//                        saturdayHours.text = "Saturday: ${timeSlots[10]} - ${timeSlots[11]}"
//                        sundayHours.text = "Sunday: ${timeSlots[12]} - ${timeSlots[13]}"
//                    }
//
//
//                } else {
//                    Toast.makeText(this, "Branch data not found.", Toast.LENGTH_LONG).show()
//                }
//            }
//        return documentFound
//    }

//    private fun fetchAndWriteUserData() {
//        userInfo.get()
//            .addOnSuccessListener {documentSnapshot ->
//                if (documentSnapshot.exists()) {
//                    val data = documentSnapshot.data
//                    val role = data?.get("role") as? String
//                    val userName = data?.get("userName") as? String
//                    //display data in welcome message
//                    welcomeMessage = findViewById(R.id.welcomeMessage)
//                    welcomeMessage.text  = "Welcome $userName!"
//                    roleMessage = findViewById(R.id.roleMessage)
//                    roleMessage.text  = "Role: $role"
//                } else {
//                    Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show()
//                }
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
//            }
//    }

}