package com.example.tripit.adapters

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.tripit.R
import java.util.Timer
import java.util.TimerTask

class OnboardPagerAdapter(private val viewPager: ViewPager2) : RecyclerView.Adapter<OnboardPagerAdapter.OnboardViewHolder>() {

    private val images = intArrayOf(
        R.drawable.onboard1,
        R.drawable.onboard2,
        R.drawable.onboard3,
        R.drawable.onboard4
    )
    private val titles = arrayOf("Discover", "Plan Your Trip", "Start your Trip", "Share & Connect")
    private val descriptions = arrayOf(
        "Find inspiration, explore fascinating destination from our App.",
        "Select destinations and start scheduling details for your trip.",
        "Enjoy! Relax and chilling memories \n Enjoy! Relax.",
        "Share your experiences on the trip & connect with travelling with travellers around the Himachal Pradesh."
    )

    private val handler: Handler = Handler()
    private var currentPage = 0
    private val timer: Timer = Timer()

    // Auto-scroll time interval in milliseconds
    private val delay: Long = 3000 // 3 seconds

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.onboard_layout_viewpager, parent, false)
        return OnboardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OnboardViewHolder, position: Int) {
        holder.imageView.setImageResource(images[position])
        holder.titleTextView.text = titles[position]
        holder.descriptionTextView.text = descriptions[position]
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun startAutoScroll() {
        // Start auto-scrolling with a delay
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post {
                    currentPage = (currentPage + 1) % images.size
                    viewPager.setCurrentItem(currentPage, true)
                }
            }
        }, delay, delay)
    }

    fun stopAutoScroll() {
        timer.cancel()
    }

    class OnboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
    }
}