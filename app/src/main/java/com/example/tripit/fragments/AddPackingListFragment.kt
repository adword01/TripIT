package com.example.tripit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.tripit.R
import com.example.tripit.adapters.PackingListAdapter
import com.example.tripit.databinding.FragmentAddPackingListBinding
import com.example.tripit.dataclasses.PackingListItem
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator


class AddPackingListFragment : Fragment() {

    private lateinit var binding: FragmentAddPackingListBinding
    private lateinit var adapter: PackingListAdapter
    private lateinit var dotsIndicator: WormDotsIndicator


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPackingListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val items = listOf(
            PackingListItem.LocationItem,
            PackingListItem.CalendarItem,
            PackingListItem.TravelType,
            PackingListItem.TravelModal,
            PackingListItem.TravelLuggage,
            PackingListItem.TravelAccomodation,
            PackingListItem.TravelSummary
            // Add more items if needed
        )

        adapter = PackingListAdapter(items)
        binding.addPackingRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.addPackingRv.adapter = adapter
//        dotsIndicator.setViewPager2(binding.addPackingRv)

//        binding.btnNext.setOnClickListener {
//            val currentPosition = (binding.addPackingRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//            if (currentPosition < adapter.itemCount - 1) {
//                binding.addPackingRv.smoothScrollToPosition(currentPosition + 1)
//            }
//        }

        binding.topBack.setOnClickListener{
            val PackingListFragment = PackingListFragment()

            // Get the FragmentManager and start a FragmentTransaction
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()

            // Replace the current fragment with the "Create Post" fragment
            transaction.replace(R.id.container, PackingListFragment)
            transaction.addToBackStack(null)

            // Commit the transaction
            transaction.commit()

        }


        // Optional: Scroll up within current page to go back
        setupSwipeUpToGoBack()
    }
    private fun setupSwipeUpToGoBack() {
        binding.addPackingRv.setOnTouchListener(object : View.OnTouchListener {
            private var startY = 0f
            private val swipeThreshold = 150f

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> startY = event.y

                    MotionEvent.ACTION_UP -> {
                        val endY = event.y
                        val deltaY = startY - endY

                        val layoutManager = binding.addPackingRv.layoutManager as LinearLayoutManager
                        val currentPosition = layoutManager.findFirstVisibleItemPosition()

                        if (deltaY > swipeThreshold) {
                            // Swiped UP: Go to previous item
                            if (currentPosition < adapter.itemCount - 1) {
                                binding.addPackingRv.smoothScrollToPosition(currentPosition + 1)
                            }
                        } else if (deltaY < -swipeThreshold) {
                            // Swiped DOWN: Go to previous item
                            if (currentPosition > 0) {
                                binding.addPackingRv.smoothScrollToPosition(currentPosition - 1)
                            }
                        }
                    }
                }
                return false
            }
        })
    }

}
