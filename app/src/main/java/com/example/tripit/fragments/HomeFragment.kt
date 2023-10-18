package com.example.tripit.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tripit.ImageAdapter
import com.example.tripit.R
import com.example.tripit.databinding.FragmentHomeBinding
import kotlin.math.abs

class HomeFragment : Fragment() {

    private lateinit var viewPager2: ViewPager2

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<Int>() // Populate this list with your image resources
        imageList.add(R.drawable.hp1)
        imageList.add(R.drawable.hp2)
        imageList.add(R.drawable.hp3)
        imageList.add(R.drawable.hp4)
        imageList.add(R.drawable.hp5)
        imageList.add(R.drawable.hp6)

        val adapter = ImageAdapter(imageList)
        viewPager2 = binding.defaultViewpager
        viewPager2.adapter = adapter

        // Set the number of items to be kept in memory on each side of the current page
        viewPager2.offscreenPageLimit = 2 // Adjust as needed

        // Set page margin to create the carousel effect
        val pageMargin = resources.getDimensionPixelOffset(R.dimen.page_margin)
        viewPager2.setPageTransformer { page, position ->
            val offset = position * -pageMargin
            page.translationX = offset

            // Optionally, you can scale the pages based on their position
            val scaleFactor = 0.8f
            val scale = if (position < -1 || position > 1) {
                scaleFactor
            } else {
                (1 - abs(position)) * (1 - scaleFactor) + scaleFactor
            }
            page.scaleX = scale
            page.scaleY = scale
        }

        // Optionally, set the initial item to make the second item visible
        viewPager2.setCurrentItem(2, true)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}