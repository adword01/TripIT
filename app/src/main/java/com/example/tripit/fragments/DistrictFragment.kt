package com.example.tripit.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tripit.databinding.FragmentDistrictBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso


class DistrictFragment : Fragment() {

    private lateinit var binding: FragmentDistrictBinding

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var selectedDistrict:String
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDistrictBinding.inflate(layoutInflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("DistrictPref", Context.MODE_PRIVATE)

        val selectedDistrict = sharedPreferences.getString("district", "").toString()




        // Initialize Firebase references
        databaseReference = FirebaseDatabase.getInstance().getReference("Districts") // Replace with your actual database node
        storageReference = FirebaseStorage.getInstance().getReference("Districts") // Replace with your actual storage folder

        // Fetch data from Firebase Realtime Database
        databaseReference.child(selectedDistrict).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val district = snapshot.child("district").value.toString()
                    val description = snapshot.child("description").value.toString()
                    val imageUrl = snapshot.child("image_url").value.toString()

                    Toast.makeText(requireContext(),"$imageUrl",Toast.LENGTH_SHORT).show()
                    binding.districtName.text = district
                    binding.districtDesc.text = description

                    // Fetch and display the image from Firebase Storage using Picasso
//                    storageReference.child(imageUrl).downloadUrl.addOnSuccessListener { uri ->
                        Picasso.get().load(imageUrl).into(binding.districtImg)
//                    }.addOnFailureListener { /* Handle failure */ }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                /* Handle error */
            }
        })

        return binding.root
    }
}