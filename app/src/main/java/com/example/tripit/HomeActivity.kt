package com.example.tripit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tripit.databinding.ActivityHomeBinding
import com.example.tripit.fragments.HomeFragment
import com.example.tripit.fragments.ItineraryFragment
import com.example.tripit.fragments.PostFragment
import com.example.tripit.fragments.SettingsFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpTabBar()
        binding.chipNavigationBar.setItemSelected(R.id.menu_home, true)

        loadFragment(HomeFragment())

//        setContentView(R.layout.activity_home)
    }
    private fun setUpTabBar() {
        binding.chipNavigationBar.setOnItemSelectedListener{
            when(it){
                R.id.menu_home -> {
                    loadFragment(HomeFragment())

//                    binding.textMain.text="Home"
//                    binding.textMain.setTextColor(Color.BLUE)

                }
                R.id.menu_nav ->{
                    loadFragment(ItineraryFragment())

//                    binding.textMain.text="Message"
//                    binding.textMain.setTextColor(Color.RED)

                }
                R.id.menu_profile -> {
                    loadFragment(SettingsFragment())

//                    binding.textMain.text="Profile"
//                    binding.chipNavigationBar.showBadge(R.id.menu_settings)
//                    binding.textMain.setTextColor(Color.MAGENTA)

                }
                R.id.menu_post -> {

//                    val rootView = findViewById<View>(android.R.id.content)
//                    val snackbar = Snackbar.make(rootView, "Coming Soon", Snackbar.LENGTH_SHORT)
//                    snackbar.show()

                    loadFragment(PostFragment())

//                    Toast.makeText(this@HomeActivity,"Coming Soon",Toast.LENGTH_SHORT).show()
//                    loadFragment(PostFragment())
//                    binding.textMain.text="Settings"
//                    binding.chipNavigationBar.dismissBadge(R.id.menu_settings)
//                    binding.textMain.setTextColor(Color.GREEN)

                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
    
}