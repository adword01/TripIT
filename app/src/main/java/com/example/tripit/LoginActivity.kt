package com.example.tripit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val signin=findViewById<Button>(R.id.sign_in)
        signin.setOnClickListener {
            val intent = Intent(this@LoginActivity,HomeActivity::class.java)
            startActivity(intent)
        }

        val signup=findViewById<TextView>(R.id.signup)
        signup.setOnClickListener {
            val intent = Intent(this@LoginActivity,SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}