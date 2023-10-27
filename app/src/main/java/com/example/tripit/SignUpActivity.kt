package com.example.tripit

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tripit.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var usersRef: DatabaseReference

    private lateinit var mGoogleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance()

        // Initialize Firebase Realtime Database reference
        usersRef = FirebaseDatabase.getInstance().reference.child("users")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.login.setOnClickListener {
            startActivity(Intent(this@SignUpActivity,LoginActivity::class.java))
            finish()
        }


        binding.signup.setOnClickListener {
            signUp()
        }

        binding.googleSignin.setOnClickListener {
            // Perform Google sign-in
            googleSignIn()
        }

        binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
            binding.password.transformationMethod=HideReturnsTransformationMethod.getInstance()
        }
            else
            {
                binding.password.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    private fun signUp() {
        val name = binding.name.text.toString().trim()
        val username = binding.userName.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val phone = binding.phone.text.toString().trim()
        val password = binding.password.text.toString().trim()

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser

                    val uid = FirebaseAuth.getInstance().uid.toString()


                    user?.let {
                        // Create a HashMap to store user data
                        val userData = HashMap<String, Any>()
                        userData["name"] = name
                        userData["username"] = username
                        userData["email"] = email
                        userData["phone"] = phone
                        userData["password"] = password
                        userData["uid"] = uid

                        // Save the user data to Firebase Realtime Database
                        usersRef.child(user.uid).updateChildren(userData)

                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@SignUpActivity,HomeActivity::class.java)
                        startActivity(intent)
                        finish() // Finish the signup activity
                    }
                } else {
                    Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    companion object {
        private const val RC_GOOGLE_SIGN_IN = 9001
    }
}