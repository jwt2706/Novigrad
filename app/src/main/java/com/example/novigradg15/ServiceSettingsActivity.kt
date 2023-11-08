package com.example.novigradg15

//import android.R
import android.R.attr.data
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class ServiceSettingsActivity : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var db: DocumentReference
    private lateinit var auth: FirebaseAuth
    private lateinit var serviceListView: ListView
    private lateinit var addServiceBtn: MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_settings)

        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid
        db = FirebaseFirestore.getInstance().collection("users").document(userId)
        addServiceBtn = findViewById(R.id.addServiceBtn)

        addServiceBtn.setOnClickListener {
            //if "ADD SERVICE" clicked, open the add service activity page
            addServiceBtnListener()
        }

        serviceListView = findViewById(R.id.serviceList)

        val data = arrayOf(
            ListItem("Driver's License", "24/10/23"),
            ListItem("Passport", "9/2/17")
        )

        val adapter = CustomListAdapter(this, data)
        serviceListView.adapter = adapter

        //get user data from database
        fetchAndWriteUserData()
    }

    private fun addServiceBtnListener() {
        startActivity(Intent(this,AddServiceActivity::class.java))
        finish()
    }
    private fun fetchAndWriteUserData() {
        db.get()
            .addOnSuccessListener {documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    val role = data?.get("role") as? String
                    val userName = data?.get("userName") as? String
                } else {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }
}

class CustomListAdapter(context: Context, data: Array<ListItem>) :
    BaseAdapter() {
    private val context: Context
    private val data: Array<ListItem>

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

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.service_list_item, parent, false)

        val itemText = view.findViewById<TextView>(R.id.itemText)
        val btnModify = view.findViewById<Button>(R.id.btnModify)
        val btnDelete = view.findViewById<Button>(R.id.btnDelete)

        itemText.text = listItem.name

        btnModify.setOnClickListener {
            // Handle the "Modify" button click for this item
        }

        btnDelete.setOnClickListener {
            // Handle the "Delete" button click for this item
        }

        return view
    }
}

class ListItem(val name: String, val date: String)