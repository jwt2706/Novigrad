package com.example.novigradg15

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.common.api.Api.Client
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class RateBranchActivity : AppCompatActivity() {
    private lateinit var branchName: String
    private lateinit var branchLabel: TextView
    private lateinit var ratingInput: EditText
    private lateinit var submitBtn: MaterialButton

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, ClientWelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_branch)

        branchLabel = findViewById(R.id.branchLabel)
        ratingInput = findViewById(R.id.ratingInput)
        submitBtn = findViewById(R.id.submitBtn)

        branchName = intent.getStringExtra("branchName")?: ""

        submitBtn.setOnClickListener {
            fetchAndUpdateReviewsData()
            var intent = Intent(this, ClientWelcomeActivity::class.java)
            startActivity(intent);
            }
        }

    public fun fetchAndUpdateReviewsData(): Boolean {
        var success = false
        val db = FirebaseFirestore.getInstance().collection("reviews")
        db.get().addOnSuccessListener { snapshot ->
            success = true
            var documentUpdated = false
            for (document in snapshot) {
                val data = document.data
                val selectedBranchName = data?.get("branchName") as? String
                if (selectedBranchName.toString().trim() == branchName.trim()) {
                    val reviews = data["reviews"] as? Double ?: 0.0
                    val reviewCount = data["reviewCount"] as? Double ?: 0.0
                    val newReviews = ((reviewCount * reviews) + (ratingInput.text.toString().toDouble())) / (reviewCount + 1)
                    documentUpdated = true
                    document.reference.update(mapOf(
                        "reviews" to newReviews,
                        "reviewCount" to reviewCount + 1
                    )).addOnSuccessListener {
                    }.addOnFailureListener {
                    }
                    break
                }
            }

            if (!documentUpdated) {
                createNewDocument(db)
            }
        }.addOnFailureListener { e ->
        // Handle failure
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
        }
        return success
    }

    public fun createNewDocument(db: CollectionReference): Boolean {
        var success = false
        val newReviewScore = ratingInput.text.toString().toDouble()
        val hMap = hashMapOf(
            "branchName" to branchName,
            "reviews" to newReviewScore,
            "reviewCount" to 1
        )

        db.add(hMap).addOnSuccessListener {
            // Handle success
            success = true
        }.addOnFailureListener {
            // Handle failure
            success = false
        }
        return success
    }
}