package com.example.tripit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import club.cred.neopop.PopFrameLayout
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class OnboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard)

        val viewPager: ViewPager2 = findViewById(R.id.viewpager)
        val dotsIndicator: DotsIndicator = findViewById(R.id.dots_indicator)
        val adapter = OnboardPagerAdapter(viewPager)
        viewPager.adapter = adapter
        adapter.startAutoScroll()


        // Attach the dot indicator to the ViewPager
        dotsIndicator.setViewPager2(viewPager)


        val login = findViewById<PopFrameLayout>(R.id.cred_view)
        login.setOnClickListener {
            val intent = Intent(this@OnboardActivity,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}