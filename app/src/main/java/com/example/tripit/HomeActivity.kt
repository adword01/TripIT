package com.example.tripit

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tripit.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpTabBar()

//        setContentView(R.layout.activity_home)
    }
    private fun setUpTabBar() {
        binding.chipNavigationBar.setOnItemSelectedListener{
            when(it){
                R.id.menu_home -> {
//                    binding.textMain.text="Home"
//                    binding.textMain.setTextColor(Color.BLUE)

                }
                R.id.menu_message ->{
//                    binding.textMain.text="Message"
//                    binding.textMain.setTextColor(Color.RED)

                }
                R.id.menu_profile -> {
//                    binding.textMain.text="Profile"
//                    binding.chipNavigationBar.showBadge(R.id.menu_settings)
//                    binding.textMain.setTextColor(Color.MAGENTA)

                }
                R.id.menu_settings -> {
//                    binding.textMain.text="Settings"
//                    binding.chipNavigationBar.dismissBadge(R.id.menu_settings)
//                    binding.textMain.setTextColor(Color.GREEN)

                }
            }
        }
    }
}