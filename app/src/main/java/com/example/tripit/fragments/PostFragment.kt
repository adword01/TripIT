package com.example.tripit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.tripit.R
import com.example.tripit.databinding.FragmentPostBinding


class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=  FragmentPostBinding.inflate(inflater,container,false)

        binding.addNew.setOnClickListener {

            val createPostFragment = CreatePostFragment()

            // Get the FragmentManager and start a FragmentTransaction
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()

            // Replace the current fragment with the "Create Post" fragment
            transaction.replace(R.id.container, createPostFragment)
            // You can add the transaction to the back stack if you want to navigate back
            // transaction.addToBackStack(null)

            // Commit the transaction
            transaction.commit()
        }

        return binding.root
    }


}