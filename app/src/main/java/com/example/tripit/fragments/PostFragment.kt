package com.example.tripit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.tripit.PostAdapter
import com.example.tripit.R
import com.example.tripit.databinding.FragmentPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding

    private val postsData: MutableList<HashMap<String, Any>> = ArrayList()

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var useruid: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=  FragmentPostBinding.inflate(inflater,container,false)

        firebaseAuth = FirebaseAuth.getInstance()

        useruid = firebaseAuth.currentUser?.uid.toString()

        retrievePostsFromDatabase()

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

    private fun retrievePostsFromDatabase() {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("posts").child(useruid)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postsData.clear() // Clear the list to avoid duplicates

                for (postSnapshot in dataSnapshot.children) {
                    val postMap = HashMap<String, Any>()
                    postMap["username"] = postSnapshot.child("username").value.toString()
                    postMap["uid"] = postSnapshot.child("uid").value.toString()
                    postMap["post_number"] = postSnapshot.child("post_number").value as Long
                    postMap["content"] = postSnapshot.child("content").value.toString()
                    postMap["location"] = postSnapshot.child("location").value.toString()
                    postMap["imageUrl"] = postSnapshot.child("imageUrl").value.toString()

                    postsData.add(postMap)
                }

                // Create and set the adapter with the retrieved data
                val adapter = PostAdapter(postsData)
                binding.postRv.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }



}