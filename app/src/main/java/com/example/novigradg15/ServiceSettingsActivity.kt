package com.example.novigradg15

//import android.R
import android.R.attr.data
import android.R.attr.listSeparatorTextViewStyle
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class ServiceSettingsActivity : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var userData: DocumentReference
    private lateinit var db: CollectionReference
    private lateinit var auth: FirebaseAuth
    private lateinit var serviceListView: ListView
    private lateinit var addServiceBtn: MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_settings)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance().collection("services")
        addServiceBtn = findViewById(R.id.addServiceBtn)

        addServiceBtn.setOnClickListener {
            addServiceBtnListener()
        }

        serviceListView = findViewById(R.id.serviceList)

        val data = ArrayList<ListItem>();
        db.get() //get all services from the database
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    val item = ListItem(
                        "Name",
                        true,
                        true,
                        true,
                        true,
                        "Additional Information"
                    )
                    data.add(item)
                }
            }
            .addOnFailureListener{ e ->
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()

            }

        val adapter = CustomListAdapter(this, data)
        serviceListView.adapter = adapter

    }

    private fun addServiceBtnListener() {
        startActivity(Intent(this,AddServiceActivity::class.java))
        finish()
    }
}

class CustomListAdapter(context: Context, data: ArrayList<ListItem>) :
    BaseAdapter() {
    private val context: Context
    private val data: ArrayList<ListItem>

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
        val documentsValue = view.findViewById<TextView>(R.id.documentsValue)
        val formValue = view.findViewById<TextView>(R.id.formValue)
        val statusValue = view.findViewById<TextView>(R.id.statusValue)
        val photoValue = view.findViewById<TextView>(R.id.photoValue)
        val additionalInfoValue = view.findViewById<TextView>(R.id.additionalInfoValue)
        val btnModify = view.findViewById<Button>(R.id.btnModify)
        val btnDelete = view.findViewById<Button>(R.id.btnDelete)

        itemText.text = listItem.name
        documentsValue.text = listItem.documentsUsed.toString()
        formValue.text = listItem.formUsed.toString()
        statusValue.text = listItem.statusUsed.toString()
        photoValue.text = listItem.photoUsed.toString()
        additionalInfoValue.text = listItem.additionalInformation


        btnModify.setOnClickListener {
            // Handle the "Modify" button click for this item
        }

        btnDelete.setOnClickListener {
            // Handle the "Delete" button click for this item
        }

        return view
    }
}

class ListItem(
    val name: String,
    val documentsUsed: Boolean,
    val formUsed: Boolean,
    val statusUsed: Boolean,
    val photoUsed: Boolean,
    val additionalInformation: String
    )