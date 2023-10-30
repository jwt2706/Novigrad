package com.example.novigradg15

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SignupActivity : AppCompatActivity() {

    private lateinit var username: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var licenseLevel: String
    private lateinit var role: String

    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var licensePreview: ImageView
    private lateinit var uploadLicenseBtn: MaterialButton
    private lateinit var signupBtn: MaterialButton

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        licensePreview = findViewById(R.id.imagePreview)
        uploadLicenseBtn = findViewById(R.id.uploadImageButton)
        signupBtn = findViewById(R.id.signupbtn)

        auth = FirebaseAuth.getInstance()

        // Prepares activity that shows image upload options
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode != Activity.RESULT_OK) {
                    Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show()
                } else {
                    val uri = it.data?.data
                    loadImageFromUri(this, licensePreview, uri)
                }
            }

        uploadLicenseBtn.setOnClickListener {
            this.uploadLicenseBtnListener()
        }

        signupBtn.setOnClickListener {
            this.signupBtnListener()
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

    private fun signupBtnListener() {
        this.username = findViewById<EditText>(R.id.usernameInput).getText().toString()
        this.email = findViewById<EditText>(R.id.emailInput).getText().toString()
        this.password = findViewById<EditText>(R.id.passwordInput).getText().toString()
        this.licenseLevel = findViewById<Spinner>(R.id.driverLicenseSpinner).selectedItem.toString()
        this.role = findViewById<Spinner>(R.id.roleSpinner).selectedItem.toString()

        //attempts to create an account on the database with the submitted credentials
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful) {

                val userId = auth.currentUser?.uid
                val db = FirebaseFirestore.getInstance().collection("users")

                val user = hashMapOf(
                    "userName" to username,
                    "email" to email,
                    "role" to role,
                    "licenseLevel" to licenseLevel,
                )
                db.document(userId!!)
                    .set(user)
                    .addOnCompleteListener {saveData ->
                        if (saveData.isSuccessful) {

                            Toast.makeText(this, "Account creation successful.", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()

                        } else {
                            Toast.makeText(this, "Account creation failed. Please try again.", Toast.LENGTH_LONG).show()

                        }
                    }
            }
        }.addOnFailureListener { e ->
            Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadLicenseBtnListener() {
        pickImageLauncher.launch(
            Intent(MediaStore.ACTION_PICK_IMAGES)
        )
    }

}
