package com.example.tripit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import club.cred.neopop.PopFrameLayout
import com.example.tripit.adapters.OnboardPagerAdapter
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class OnboardActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard)

        sharedPreferences = getSharedPreferences("onboard_prefs", Context.MODE_PRIVATE)

        // Check if the app has been onboarded before
        if (sharedPreferences.getBoolean("onboarded", false)) {
            // If onboarded, start the LoginActivity and finish this activity
            startLoginActivity()
            return
        }

        val viewPager: ViewPager2 = findViewById(R.id.viewpager)
        val dotsIndicator: DotsIndicator = findViewById(R.id.dots_indicator)
        val adapter = OnboardPagerAdapter(viewPager)
        viewPager.adapter = adapter
        adapter.startAutoScroll()

        dotsIndicator.setViewPager2(viewPager)

        val login = findViewById<PopFrameLayout>(R.id.cred_view)
        login.setOnClickListener {
            // Set onboarded to true and start LoginActivity
            sharedPreferences.edit().putBoolean("onboarded", true).apply()
            startLoginActivity()
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(this@OnboardActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}