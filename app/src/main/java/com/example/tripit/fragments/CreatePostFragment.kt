package com.example.tripit.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.tripit.databinding.FragmentCreatePostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class CreatePostFragment : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentCreatePostBinding
    private lateinit var useruid: String
    private lateinit var imageUrl: String
    private lateinit var ProfileImage : String
    private var post_number:Int = 0
    private var currentPostNumber:Int=0

    companion object {
        const val REQUEST_IMAGE_PICK = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePostBinding.inflate(layoutInflater, container, false)

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        firebaseAuth = FirebaseAuth.getInstance()

        useruid = firebaseAuth.currentUser?.uid.toString()

        getUserInfo()


        checkProfileImageUrlInDatabase()

        binding.imageView11.setOnClickListener {
            pickImageFromGallery()
        }
        binding.uploadBtn.setOnClickListener {
            retrievePostNumber()
        }



        return binding.root
    }

    private fun pickImageFromGallery() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_IMAGE_PICK
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_IMAGE_PICK) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: String = data.data.toString()
            imageUrl = imageUri

            Picasso.get().load(imageUri).into(binding.imageView11)

            binding.locationLyt.visibility = View.VISIBLE
        }
    }

    private fun getUserInfo() {
        val usernameRef = databaseReference.child(useruid).child("username")

        usernameRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val username = dataSnapshot.value.toString()
                binding.userName.text = username
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here
            }
        })
    }

    private fun checkProfileImageUrlInDatabase() {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        databaseReference.child(useruid).child("profileImageUrl")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    ProfileImage = dataSnapshot.value.toString()

                    Picasso.get().load(ProfileImage).into(binding.userProfileImg)

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors here
                }
            })
    }

    private fun savePostToDatabase(post_number : Int) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("posts")


        val postKey = databaseReference.child("posts")
        val postMap = HashMap<String, Any>()

        postMap["username"] = binding.userName.text.toString()
        postMap["ProfileImage"] = ProfileImage
        postMap["post_number"] = post_number
        postMap["content"] = binding.caption.text.toString()
        postMap["location"] = binding.locationTxt.text.toString()
        postMap["imageUrl"] = imageUrl // Use the profile image URL

        databaseReference.child(useruid).child(post_number.toString()).setValue(postMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    AddPostNumber(post_number)
                    Toast.makeText(requireContext(), "Post saved successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error saving post", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun AddPostNumber(postNumber : Int){
        databaseReference.child("PostNumber").child(useruid).setValue(hashMapOf(
            "Post" to postNumber
        ))
    }

    private fun retrievePostNumber() {


        val postReference = databaseReference.child("PostNumber").child(useruid)


        postReference.get().addOnSuccessListener {
            if (it.exists()){
                val postNumber = it.child("Post").value
                currentPostNumber = postNumber.toString().toInt()
            }else{
                currentPostNumber = 0
            }
        }

        currentPostNumber += 1
        savePostToDatabase(currentPostNumber)

//        postReference.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()){
//
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })




//        val postNumberReference = databaseReference.child("posts").child(useruid).child(post_number.toString()).child("post_number")
//
//        postNumberReference.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    currentPostNumber = (dataSnapshot.value as Long).toInt()
////                    Log.d("PostNumber", "Current Post Number: $currentPostNumber")
////
////                    post_number++
////                    Log.d("PostNumber", "Updated Post Number: $post_number")
////
////                    savePostToDatabase()
//                } else {
//                    currentPostNumber=0
////                    Log.d("PostNumber", "Post Number doesn't exist. Setting to 1")
////                    savePostToDatabase()
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle any errors that occurred during the database operation
//                Log.e("PostNumber", "Error retrieving post number: ${databaseError.message}")
//            }
//        })
//        if (currentPostNumber == 0){
//            currentPostNumber++
//            savePostToDatabase(currentPostNumber)
//        }
//        else{
//            currentPostNumber++
//            Log.d("PostNumber", "Updated Post Number: $currentPostNumber")
//
//            savePostToDatabase(currentPostNumber)
//        }
    }





}