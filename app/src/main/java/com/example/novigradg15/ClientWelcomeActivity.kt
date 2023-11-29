package com.example.novigradg15

import android.content.Context
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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ClientWelcomeActivity : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var db: DocumentReference
    private lateinit var auth: FirebaseAuth
    private lateinit var branchesListView: ListView
    private lateinit var originalBranchesList: ArrayList<BranchSearchListItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_welcome)

        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid
        db = FirebaseFirestore.getInstance().collection("users").document(userId)

        val filterBtn = findViewById<MaterialButton>(R.id.filterButton)
        filterBtn.setOnClickListener {
            startActivity(Intent(this, SearchFiltersActivity::class.java))
            finish()
        }

        //get user data from database
        fetchAndWriteUserData()

        branchesListView = findViewById(R.id.branchesList)
        originalBranchesList = ArrayList();

        //Placeholders
        originalBranchesList.add(
            BranchSearchListItem(
                "Branch Name 1",
                "221 Green Branch",
                "4444444444",
                "Health Card"
            )
        )
        originalBranchesList.add(
            BranchSearchListItem(
                "Branch Name 2",
                "221 Blue Branch",
                "5555555555",
                "ID Card"
            )
        )
        originalBranchesList.add(
            BranchSearchListItem(
                "Branch Name 3",
                "221 Yellow Branch",
                "3333333333",
                "ID Card, Driving License"
            )
        )
        originalBranchesList.add(
            BranchSearchListItem(
                "Branch Name 4",
                "221 Branch Address",
                "613 555 9832",
                "Health Card, ID Card, Driving License"
            )
        )
        val adapter = BranchesListAdapter(this, ArrayList(originalBranchesList))
        branchesListView.adapter = adapter
        setupSearchFilter()

        applyFilters()
    }

    private fun applyFilters() {
        val branchAddress = intent.getStringExtra("branchAddress")
        val branchTelephone = intent.getStringExtra("branchTelephone")
//        val dayOfTheWeek = intent.getStringExtra("dayOfTheWeek")
//        val time = intent.getStringExtra("time")
        val servicesList: ArrayList<String>? = intent.getStringArrayListExtra("services")

        Log.d("TAG1", servicesList.toString())
        if (servicesList != null) {
            Log.d("TAG2", servicesList.size.toString())
        }

        if (!branchAddress.isNullOrBlank()) {
            filterExistingByAddress(branchAddress)
        }

        if (!branchTelephone.isNullOrBlank()) {
            filterExistingByTelephone(branchTelephone)
        }

        if (!servicesList.isNullOrEmpty() && servicesList[0] != "") {
            filterExistingByServices(servicesList.joinToString(", "))
        }
    }

    fun fetchAndWriteUserData():Boolean {
        var success = false
        db.get()
            .addOnSuccessListener {documentSnapshot ->
                if (documentSnapshot.exists()) {
                    success = true
                    val data = documentSnapshot.data
                    //display data in welcome message
                } else {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        return success
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
            modifiedBranchList.filter { it.branchName.contains(query, ignoreCase = true) }
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
            modifiedBranchList.filter { it.addressValue.contains(query, ignoreCase = true) }
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
            modifiedBranchList.filter { it.telephoneValue.contains(query, ignoreCase = true) }
        }
        adapter.refreshData(filteredBranches)
    }

    private fun filterExistingByServices(query: String) {
        val listView = findViewById<ListView>(R.id.branchesList)
        val adapter = listView.adapter as BranchesListAdapter
        var modifiedBranchList = adapter.data
        val filteredBranches = if (query.isEmpty()) {
            modifiedBranchList
        } else {
            modifiedBranchList.filter {
                it.servicesValue.contains(query, ignoreCase = true)
            }
        }
        adapter.refreshData(filteredBranches)
    }
}

class BranchesListAdapter(context: Context, data: ArrayList<BranchSearchListItem>) :
    BaseAdapter() {
    private val context: Context
    val data: ArrayList<BranchSearchListItem>

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

    fun refreshData(newBranches: List<BranchSearchListItem>) {
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

        branchName.text = listItem.branchName
        addressValue.text = listItem.addressValue
        telephoneValue.text = listItem.telephoneValue
        servicesValue.text = listItem.servicesValue

        btnRequest.setOnClickListener {
            // Request service logic
        }
        return view
    }
}

class BranchSearchListItem(
    val branchName: String,
    val addressValue: String,
    val telephoneValue: String,
    val servicesValue: String,
)

