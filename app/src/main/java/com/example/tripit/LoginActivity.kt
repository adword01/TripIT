package com.example.tripit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.tripit.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            signIn.setOnClickListener {
                if (loginEmail.text.toString() == "user@example.com" && LoginPassword.text.toString() == "123456"){
                    val intent = Intent(this@LoginActivity,HomeActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this@LoginActivity,"Logged in successfully",Toast.LENGTH_SHORT).show()
                }

            }



            signup.setOnClickListener {
                val intent = Intent(this@LoginActivity,SignUpActivity::class.java)
                startActivity(intent)
            }
        }


    }
}