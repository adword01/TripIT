package com.example.tripit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import com.example.tripit.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RatingDialogFragment : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rating_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

                val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)

        // Set up the RatingBar
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            // Handle rating changes here
            Toast.makeText(requireContext(),"$rating",Toast.LENGTH_SHORT).show()
        }
    }
}