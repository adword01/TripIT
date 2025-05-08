package com.example.tripit.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.tripit.R
import com.example.tripit.databinding.FragmentPackingListBinding

class PackingListFragment : Fragment() {

    private lateinit var binding: FragmentPackingListBinding

    companion object {
        fun newInstance() = PackingListFragment()
    }

    private val viewModel: PackingListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPackingListBinding.inflate(inflater, container, false)

        binding.addPackingListFab.setOnClickListener {

            val PackingListFragment = AddPackingListFragment()

            // Get the FragmentManager and start a FragmentTransaction
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()

            // Replace the current fragment with the "Create Post" fragment
            transaction.replace(R.id.container, PackingListFragment)
            transaction.addToBackStack(null)

            // Commit the transaction
            transaction.commit()
        }

        return binding.root
    }
}