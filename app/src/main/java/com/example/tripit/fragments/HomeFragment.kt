package com.example.tripit.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.tripit.ImageAdapter
import com.example.tripit.R
import com.example.tripit.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private lateinit var imageList: ArrayList<Int>
    private lateinit var adapter: ImageAdapter
    private lateinit var runnable: Runnable

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


        val adapter = ImageAdapter(imageList, binding.defaultViewpager)
        binding.defaultViewpager.adapter = adapter

        // Set the number of items to be kept in memory on each side of the current page
        binding.defaultViewpager.offscreenPageLimit = 2

        // Set page margin to create the carousel effect
        val pageMargin = resources.getDimensionPixelOffset(R.dimen.page_margin)
        binding.defaultViewpager.setPageTransformer { page, position ->
            val offset = position * -pageMargin
            page.translationX = offset
        }

        // Optionally, set the initial item to make the second item visible
        binding.defaultViewpager.setCurrentItem(1, false)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}