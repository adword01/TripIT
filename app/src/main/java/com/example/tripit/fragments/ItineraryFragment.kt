package com.example.tripit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripit.ItineraryAdapter
import com.example.tripit.R

class ItineraryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_itinerary, container, false)

        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ItineraryAdapter(getSampleData()) // Replace with your data source
        recyclerView.adapter = adapter

        return rootView
    }

    private fun getSampleData(): List<String> {
        // Replace this with your actual data retrieval logic
        return listOf("Item 1", "Item 2", "Item 3", "Item 4")
    }
}
