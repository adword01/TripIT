package com.example.tripit.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tripit.databinding.FragmentDistrictBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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


        FetchData(databaseReference, storageReference, selectedDistrict)

//        // Fetch data from Firebase Realtime Database
//        databaseReference.child(selectedDistrict).addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    val district = snapshot.child("district").value.toString()
//                    val description = snapshot.child("description").value.toString()
//                    val imageUrl = snapshot.child("image_url").value.toString()
//
//                    Toast.makeText(requireContext(),"$imageUrl",Toast.LENGTH_SHORT).show()
//                    binding.districtName.text = district
//                    binding.districtDesc.text = description
//
//                    // Fetch and display the image from Firebase Storage using Picasso
////                    storageReference.child(imageUrl).downloadUrl.addOnSuccessListener { uri ->
//                        Picasso.get().load(imageUrl).into(binding.districtImg)
////                    }.addOnFailureListener { /* Handle failure */ }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                /* Handle error */
//            }
//        })

        return binding.root
    }


    private fun FetchData(databaseReference: DatabaseReference, storageReference: StorageReference,selectedDistrict : String){

        databaseReference.child(selectedDistrict).get().addOnSuccessListener {
            if (it.exists()){
                val district = it.child("district").value.toString()
                val districtDescription = it.child("description").value.toString()
                val imageurl = it.child("image_url").value.toString()
                val Offbeat1 = it.child("offbeat 1").value.toString()
                val Offbeat2 = it.child("offbeat 2").value.toString()
                val ts1 = it.child("tourist spot 1").value.toString()
                val ts2 = it.child("tourist spot 2").value.toString()
                val ts3 = it.child("tourist spot 3").value.toString()
                val ts4 = it.child("tourist spot 4").value.toString()
                val ts5 = it.child("tourist spot 5").value.toString()


                binding.apply {
                    districtName.text = district
                    districtDesc.text = districtDescription
                    Picasso.get().load(imageurl).into(districtImg)
                    textView1.text = Html.fromHtml("<b>Offbeat location 1</b><br/>$Offbeat1")
                    textView2.text = Html.fromHtml("<b>Offbeat location 2</b><br/>$Offbeat2")
                    textView3.text = Html.fromHtml("<b>Tourist Spot 1</b><br/>$ts1")
                    textView4.text = Html.fromHtml("<b>Tourist Spot 2</b><br/>$ts2")
                    textView5.text = Html.fromHtml("<b>Tourist Spot 3</b><br/>$ts3")
                    textView6.text = Html.fromHtml("<b>Tourist Spot 4</b><br/>$ts4")
                    textView7.text = Html.fromHtml("<b>Tourist Spot 5</b><br/>$ts5")
                    pgbar.visibility = View.GONE
                    ScrollView.visibility = View.VISIBLE
                }





            }else{
                Toast.makeText(requireContext(),"Data error",Toast.LENGTH_SHORT)
            }
        }



    }
}