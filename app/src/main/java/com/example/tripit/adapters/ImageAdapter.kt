package com.example.tripit.adapters

import android.animation.ObjectAnimator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripit.R


class ImageAdapter(private val imageList: ArrayList<Int>,private val imageName: ArrayList<String>,private val imageLocation: ArrayList<String>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val imageName: TextView = itemView.findViewById(R.id.imageName)
        val imageLocation: TextView = itemView.findViewById(R.id.imageLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_image_container, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        // Ensure all lists have the same size and check for valid index
        if (position < imageList.size && position < imageName.size && position < imageLocation.size) {
            holder.imageView.setImageResource(imageList[position])
            holder.imageName.text = imageName[position]
            holder.imageLocation.text = imageLocation[position]

            // Add fade-in animation
            fadeIn(holder.imageName)
            fadeIn(holder.imageLocation)
        } else {
            // Handle the error, e.g., log an error or notify the user
            Log.e("ImageAdapter", "Invalid index accessed in RecyclerView")
        }
    }

    private fun fadeIn(view: View) {
        val fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        fadeIn.duration = 500 // duration in milliseconds
        fadeIn.interpolator = AccelerateDecelerateInterpolator()
        fadeIn.start()
    }

    override fun getItemCount(): Int {
        // Return the smallest size among the lists to avoid IndexOutOfBoundsException
        return minOf(imageList.size, imageName.size, imageLocation.size)
    }
}