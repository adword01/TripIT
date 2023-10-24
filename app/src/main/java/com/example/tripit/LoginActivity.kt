package com.example.tripit

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tripit.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

//class LoginActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityLoginBinding
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.apply {
//            signIn.setOnClickListener {
//                if (loginEmail.text.toString() == "user@example.com" && LoginPassword.text.toString() == "123456"){
//                    val intent = Intent(this@LoginActivity,HomeActivity::class.java)
//                    startActivity(intent)
//                    Toast.makeText(this@LoginActivity,"Logged in successfully",Toast.LENGTH_SHORT).show()
//                }
//
//            }
//
//
//
//            signup.setOnClickListener {
//                val intent = Intent(this@LoginActivity,SignUpActivity::class.java)
//                startActivity(intent)
//            }
//        }
//
//
//    }
//}

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Check if the user is already signed in
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            // User is already signed in, redirect to the home screen
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.signIn.setOnClickListener {
            login()
        }

        binding.googleSignin.setOnClickListener {
            // Perform Google sign-in
            googleSignIn()
        }

        binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.loginPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.loginPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    private fun login() {
        val email = binding.loginEmail.text.toString().trim()
        val password = binding.loginPassword.text.toString().trim()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish() // Finish the login activity
                } else {
                    Toast.makeText(this, "Login failed. Please check your email and password.", Toast.LENGTH_SHORT).show()
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
