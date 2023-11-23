package com.example.novigradg15

//import android.R
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
import com.google.firebase.auth.FirebaseUser


class BranchSettingsActivity : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var userData: DocumentReference
    private lateinit var db: CollectionReference
    private lateinit var auth: FirebaseAuth
    private lateinit var serviceListView: ListView
    private lateinit var addServiceBtn: MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_branch_settings)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance().collection("users")
        serviceListView = findViewById(R.id.serviceList)

        var data = ArrayList<BranchListItem>();
        db.get() //get all services from the database
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    val item = BranchListItem(
                        doc.getString("userName")?: "",
                        doc.getString("email")?: "",
                        doc.reference.id
                    )
                    if (doc.getString("role") == "Employee") {
                        data.add(item)
                        Log.d("Tag222", doc.toString())
                    }
                }
                val adapter = BranchCustomListAdapter(this, data)
                serviceListView.adapter = adapter
            }
            .addOnFailureListener{ e ->
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
            }

    }
}
class BranchCustomListAdapter(context: Context, data: ArrayList<BranchListItem>) :
    BaseAdapter() {
    private val context: Context
    private val data: ArrayList<BranchListItem>

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

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.branch_account_list_item, parent, false)

        val itemUsername = view.findViewById<TextView>(R.id.itemUsername)
        val itemEmail = view.findViewById<TextView>(R.id.itemEmail)
        val btnDelete = view.findViewById<Button>(R.id.btnDelete)

        itemUsername.text = listItem.name
        itemEmail.text = listItem.email

        btnDelete.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val collectionReference = db.collection("users")
            val documentReference = collectionReference.document(listItem.id)
            val userID = listItem.id
            documentReference.delete()
                .addOnSuccessListener {
                    data.remove(listItem) // Remove the item from the data list
                    notifyDataSetChanged()
                    Toast.makeText(context, "Account deleted.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }

        return view
    }
}

class BranchListItem(
    val name: String,
    val email: String,
    val id: String
)
