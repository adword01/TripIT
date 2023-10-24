package com.example.tripit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tripit.R
import com.example.tripit.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private lateinit var databaseReference:DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentSettingsBinding.inflate(layoutInflater, container, false)

        binding.progressBar.visibility = View.VISIBLE

        binding.profile.setOnClickListener {
            loadFragment(ProfileFragment())
        }

        checkProfileImageUrlInDatabase()

        return binding.root
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
    private fun checkProfileImageUrlInDatabase() {

        firebaseAuth = FirebaseAuth.getInstance()

        val useruid = firebaseAuth.currentUser?.uid.toString()

        // Check if the profile image URL exists in the database
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        databaseReference.child(useruid).child("profileImageUrl")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val imageUrl = dataSnapshot.value.toString()
                    // If a profile image URL is available in the database, load the image
                    Picasso.get().load(imageUrl).into(binding.profilePic)
                    binding.progressBar.visibility = View.GONE


                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors here
                    binding.progressBar.visibility = View.GONE

                }
            })
    }
}