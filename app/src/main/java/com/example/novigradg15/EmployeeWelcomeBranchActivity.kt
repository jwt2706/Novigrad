package com.example.novigradg15

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
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
    private lateinit var branchNameValue: String
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
    private lateinit var serviceRequestListView: ListView

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

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
                    branchNameValue = name.toString()
                    val address = data?.get("address") as? String
                    val telephone = data?.get("telephone") as? String
                    val timeSlots = data?.get("timeSlots") as? List<String>
                    val services = data?.get("services") as? List<String>

                    branchName.text = "Name: $name"
                    branchAddress.text = "Address: $address"

                    if (telephone != null) {
                        branchTelephone.text = "Telephone: ${telephone.substring(0, 3)} ${telephone.substring(3, 6)} ${telephone.substring(6, 10)}"
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

                    fetchAndWriteRequestsData()

                } else {
                    Toast.makeText(this, "Branch data not found.", Toast.LENGTH_LONG).show()
                }

            }
        return documentFound
    }

    private fun fetchAndWriteRequestsData() {
        serviceRequestListView = findViewById(R.id.serviceRequestList)
        //list of the requests fetched from the DB
        val db = FirebaseFirestore.getInstance().collection("requests")
        db.get()
            .addOnSuccessListener { snapshot ->
                var serviceRequests = ArrayList<ServiceRequestListItem>();
                for (document in snapshot) {
                    val data = document.data
                    val selectedBranchName = data?.get("branchName") as? String
                    Log.d("!!!!", selectedBranchName.toString())
                    Log.d("????", branchNameValue)
                        if (selectedBranchName.toString().trim() == branchNameValue.trim()) {
                            val item = ServiceRequestListItem(
                                document.id,
                                data?.get("customerName") as? String ?: "",
                                "${data?.get("date") as? String ?: ""} at ${data?.get("timeSelected") as String?: ""}",
                                data?.get("selectedService") as? String ?: "",
                                data?.get("email") as? String ?: "",
                                data?.get("dateOfBirth") as? String ?: "",
                                data?.get("address") as? String ?: "",
                                data?.get("license") as? String ?: "",
                            )
                            serviceRequests.add(item)
                            Log.d("@@@@@@2", serviceRequests.toString())
                        }
                }
                Log.d("@@@@@@", serviceRequests.toString())
                val adapter = ServiceRequestCustomListAdapter(this, serviceRequests)
                serviceRequestListView.adapter = adapter

                setListViewHeightBasedOnChildren(serviceRequestListView)
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
                    Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }

    //FIX FOR A UI GLITCH
    //Makes services list show completely without the need to scroll
    fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter ?: return

        var totalHeight = 0
        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = totalHeight + (listView.dividerHeight * (listAdapter.count - 1))
        listView.layoutParams = params
        listView.requestLayout()
    }
}

class ServiceRequestCustomListAdapter(context: Context, data: ArrayList<ServiceRequestListItem>) :
    BaseAdapter() {
    private val context: Context
    private val data: ArrayList<ServiceRequestListItem>

    init {
        this.context = context
        this.data = data
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val listItem = data[position]

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.service_request_list_item, parent, false)

        val customerName = view.findViewById<TextView>(R.id.customerName)
        val appointmentValue = view.findViewById<TextView>(R.id.appointmentValue)
        val serviceValue = view.findViewById<TextView>(R.id.serviceValue)
        val emailValue = view.findViewById<TextView>(R.id.emailValue)
        val dobValue = view.findViewById<TextView>(R.id.dateValue)
        val addressValue = view.findViewById<TextView>(R.id.addressValue)
        val licenseValue = view.findViewById<TextView>(R.id.licenseValue)
        val btnAccept = view.findViewById<Button>(R.id.btnAccept)
        val btnDecline = view.findViewById<Button>(R.id.btnDecline)

        customerName.text = listItem.customerName
        appointmentValue.text = listItem.appointmentDate
        serviceValue.text = listItem.serviceRequired
        emailValue.text = listItem.customerEmail
        dobValue.text = listItem.customerDateOfBirth
        addressValue.text = listItem.customerAddress
        licenseValue.text = listItem.customerLicense


        btnAccept.setOnClickListener {
            val db = FirebaseFirestore.getInstance().collection("requests")
            db.document(listItem.documentId).delete().addOnSuccessListener {
                Toast.makeText(context, "Service Accepted", Toast.LENGTH_SHORT).show()
                data.removeAt(position)
                notifyDataSetChanged()
            }.addOnFailureListener {
                Toast.makeText(context, "Service could not be accepted", Toast.LENGTH_SHORT).show()
            }
            // BONUS: Notify user
        }

        btnDecline.setOnClickListener {
            val db = FirebaseFirestore.getInstance().collection("requests")
            db.document(listItem.documentId).delete().addOnSuccessListener {
                Toast.makeText(context, "Service Declined", Toast.LENGTH_SHORT).show()
                data.removeAt(position)
                notifyDataSetChanged()
            }.addOnFailureListener {
                Toast.makeText(context, "Service could not be declined", Toast.LENGTH_SHORT).show()
            }
            // BONUS: Notify user
        }
        return view
    }
}

class ServiceRequestListItem(
    val documentId: String,
    val customerName: String,
    val appointmentDate: String,
    val serviceRequired: String,
    val customerEmail: String,
    val customerDateOfBirth: String,
    val customerAddress: String,
    val customerLicense: String
)
