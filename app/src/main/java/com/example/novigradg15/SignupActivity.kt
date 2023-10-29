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

    private lateinit var pickSingleMediaLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val licensePreview = findViewById<ImageView>(R.id.imagePreview)
        val uploadLicenseBtn = findViewById<MaterialButton>(R.id.uploadImageButton)

        val auth = FirebaseAuth.getInstance()
        val signupBtn = findViewById<MaterialButton>(R.id.signupbtn)

        // Allows loading an image in an ImageView from a Uri
        fun loadBitmapFromUri(context: Context, image: ImageView, uri: Uri?) {
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

        // Prepares activity that shows image upload options
        pickSingleMediaLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode != Activity.RESULT_OK) {
                    Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show()
                } else {
                    val uri = it.data?.data
                    loadBitmapFromUri(this, licensePreview, uri)
                }
            }

        uploadLicenseBtn.setOnClickListener {
            pickSingleMediaLauncher.launch(
                Intent(MediaStore.ACTION_PICK_IMAGES)
            )
        }

        signupBtn.setOnClickListener {
            val userName: String = findViewById<EditText>(R.id.usernameInput).getText().toString()
            val email: String = findViewById<EditText>(R.id.emailInput).getText().toString()
            val password: String = findViewById<EditText>(R.id.passwordInput).getText().toString()
            val licenseLevel: String = findViewById<Spinner>(R.id.driverLicenseSpinner).selectedItem.toString()
            val role: String = findViewById<Spinner>(R.id.roleSpinner).selectedItem.toString()

            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if(task.isSuccessful) {

                    val userId = auth.currentUser?.uid
                    val db = FirebaseFirestore.getInstance().collection("users")

                    val user = hashMapOf(
                        "userName" to userName,
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
                                Toast.makeText(this, "EPIC FAIL LMAO", Toast.LENGTH_SHORT).show()

                            }
                        }
                }
            }.addOnFailureListener { e ->
                Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }

}