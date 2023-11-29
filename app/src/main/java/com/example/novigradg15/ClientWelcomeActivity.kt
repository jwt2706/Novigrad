package com.example.novigradg15

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ClientWelcomeActivity : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var db: CollectionReference
    private lateinit var auth: FirebaseAuth
    private lateinit var branchesListView: ListView
    private lateinit var originalBranchesList: ArrayList<Map<String, Any>>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_welcome)

        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid

        val filterBtn = findViewById<MaterialButton>(R.id.filterButton)
        filterBtn.setOnClickListener {
            startActivity(Intent(this, SearchFiltersActivity::class.java))
            finish()
        }

        branchesListView = findViewById(R.id.branchesList)
        originalBranchesList = ArrayList()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance().collection("branches")
        db.get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    originalBranchesList.add(doc.data)
                }

                val adapter = BranchesListAdapter(this, ArrayList(originalBranchesList))
                branchesListView.adapter = adapter
                setupSearchFilter()
                applyFilters()
            }
            .addOnFailureListener{ e ->
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
            }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun applyFilters() {
        val branchAddress = intent.getStringExtra("branchAddress")
        val branchTelephone = intent.getStringExtra("branchTelephone")
        val dayOfTheWeek = intent.getStringExtra("dayOfTheWeek")
        val time = intent.getStringExtra("time")
        val service = intent.getStringExtra("service")

        if (!branchAddress.isNullOrBlank()) {
            Log.d("!!!", "HERE")
            filterExistingByAddress(branchAddress)
        }

        if (!branchTelephone.isNullOrBlank()) {
            Log.d("!!!", "HERE2")
            filterExistingByTelephone(branchTelephone)
        }

        if (!dayOfTheWeek.isNullOrBlank() && !time.isNullOrBlank()) {
            filterExistingByDate(dayOfTheWeek, time)
        }

        if (!service.isNullOrBlank() && service != "None") {
            filterExistingByService(service)
        }


    }

    private fun setupSearchFilter() {
        val searchField = findViewById<EditText>(R.id.searchField)
        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterBranches(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Not used
            }
        })
    }

    private fun filterBranches(query: String) {
        val listView = findViewById<ListView>(R.id.branchesList)
        val adapter = listView.adapter as BranchesListAdapter
        var modifiedBranchList = ArrayList(originalBranchesList)
        val filteredBranches = if (query.isEmpty()) {
            modifiedBranchList
        } else {
            modifiedBranchList.filter {
                var branchName = it?.get("name") as? String?:""
                branchName.contains(query, ignoreCase = true)
            }
        }
        adapter.refreshData(filteredBranches)
    }

    private fun filterExistingByAddress(query: String) {
        val listView = findViewById<ListView>(R.id.branchesList)
        val adapter = listView.adapter as BranchesListAdapter
        var modifiedBranchList = adapter.data
        val filteredBranches = if (query.isEmpty()) {
            modifiedBranchList
        } else {
            modifiedBranchList.filter {
                var branchAddress = it?.get("address") as? String?:""
                branchAddress.contains(query, ignoreCase = true)
            }
        }
        adapter.refreshData(filteredBranches)
    }

    private fun filterExistingByTelephone(query: String) {
        val listView = findViewById<ListView>(R.id.branchesList)
        val adapter = listView.adapter as BranchesListAdapter
        var modifiedBranchList = adapter.data
        val filteredBranches = if (query.isEmpty()) {
            modifiedBranchList
        } else {
            modifiedBranchList.filter {
                var branchTelephone = it?.get("telephone") as? String?:""
                branchTelephone.contains(query, ignoreCase = true)
            }
        }
        adapter.refreshData(filteredBranches)
    }

    private fun filterExistingByService(query: String) {
        val listView = findViewById<ListView>(R.id.branchesList)
        val adapter = listView.adapter as BranchesListAdapter
        var modifiedBranchList = adapter.data
        val filteredBranches = if (query.isEmpty()) {
            modifiedBranchList
        } else {
            modifiedBranchList.filter {
                var branchServices = (it?.get("services") as? List<String>)?.joinToString(",")?: ""
                branchServices.contains(query, ignoreCase = true)
            }
        }
        adapter.refreshData(filteredBranches)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterExistingByDate(day: String, time: String) {
        val listView = findViewById<ListView>(R.id.branchesList)
        val adapter = listView.adapter as BranchesListAdapter
        var modifiedBranchList = adapter.data
        var filteredBranches: ArrayList<Map<String, Any>> = ArrayList()
        for (branch in modifiedBranchList) {
            var timeSlots = (branch?.get("timeSlots") as? List<String>)
            if (!timeSlots.isNullOrEmpty()) {
                when (day) {
                    "Monday" -> {
                        if (isTimeWithinTimeslot(timeSlots[0], timeSlots[1], time)) {
                            filteredBranches.add(branch)
                        }
                    }
                    "Tuesday" -> {
                        if (isTimeWithinTimeslot(timeSlots[2], timeSlots[3], time)) {
                            filteredBranches.add(branch)
                        }
                    }
                    "Wednesday" -> {
                        if (isTimeWithinTimeslot(timeSlots[4], timeSlots[5], time)) {
                            filteredBranches.add(branch)
                        }
                    }
                    "Thursday" -> {
                        if (isTimeWithinTimeslot(timeSlots[6], timeSlots[7], time)) {
                            filteredBranches.add(branch)
                        }
                    }
                    "Friday" -> {
                        if (isTimeWithinTimeslot(timeSlots[8], timeSlots[9], time)) {
                            filteredBranches.add(branch)
                        }
                    }
                    "Saturday" -> {
                        if (isTimeWithinTimeslot(timeSlots[10], timeSlots[11], time)) {
                            filteredBranches.add(branch)
                        }
                    }
                    "Sunday" -> {
                        if (isTimeWithinTimeslot(timeSlots[12], timeSlots[13], time)) {
                            filteredBranches.add(branch)
                        }
                    }
                    "None" -> {

                    }
                }
            }
        }
        adapter.refreshData(filteredBranches)
    }

    @RequiresApi(Build.VERSION_CODES.O) // I think this tag means it needs an a certain build number idk it made me put it
    fun isTimeWithinTimeslot(startTime: String, endTime: String, timeToCheck: String): Boolean {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val start = LocalTime.parse(startTime, timeFormatter)
        val end = LocalTime.parse(endTime, timeFormatter)
        val time = LocalTime.parse(timeToCheck, timeFormatter)
        if (time == start || time == end) return true
        if (end.isAfter(start)) {
            return time.isAfter(start) && time.isBefore(end)
        } else {
            return !time.isBefore(start) || !time.isAfter(end)
        }
    }
}

class BranchesListAdapter(context: Context, data: ArrayList<Map<String, Any>>) :
    BaseAdapter() {
    private val context: Context
    val data: ArrayList<Map<String, Any>>

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

    fun refreshData(newBranches: List<Map<String, Any>>) {
        data.clear()
        data.addAll(newBranches)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val listItem = data[position]

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.branch_search_list_item, parent, false)

        val branchName = view.findViewById<TextView>(R.id.branchName)
        val addressValue = view.findViewById<TextView>(R.id.addressValue)
        val telephoneValue = view.findViewById<TextView>(R.id.telephoneValue)
        val servicesValue = view.findViewById<TextView>(R.id.servicesValue)
        val btnRequest = view.findViewById<Button>(R.id.btnRequest)

        val serviceSpinner = view.findViewById<Spinner>(R.id.serviceSpinner)
        var availableServices = listItem?.get("services") as? List<String>
        val adapter: ArrayAdapter<String>

        if (!availableServices.isNullOrEmpty()) {
            adapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                ArrayList(availableServices)
            )
        } else {
            adapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                arrayOf("")
            )
        }


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        serviceSpinner.adapter = adapter


        branchName.text = listItem?.get("name") as? String?:""
        addressValue.text = listItem?.get("address") as? String?:""
        telephoneValue.text = listItem?.get("telephone") as? String?:""
        servicesValue.text = (listItem?.get("services") as? List<String>)?.joinToString(",")?: ""

        Log.d("SERVICES", (listItem?.get("services") as? List<String>)?.joinToString(",")?: "")
        btnRequest.setOnClickListener {
            var intent = Intent(context, RequestVisitActivity::class.java)
            val selectedService = serviceSpinner.selectedItem.toString()
            if (selectedService == "Health Card") {
                intent = Intent(context, HealthVisitActivity::class.java)
            } else if (selectedService == "ID Card") {
                intent = Intent(context, PhotoVisitActivity::class.java)
            }
            intent.putExtra("branchName", branchName.text.toString())
            intent.putExtra("selectedService", selectedService)
            context.startActivity(intent);
        }
        return view
    }
}
