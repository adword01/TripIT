package com.example.tripit.fragments

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.tripit.R
import com.example.tripit.databinding.FragmentCreatePostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CreatePostFragment : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentCreatePostBinding
    private lateinit var useruid: String
    private lateinit var imageUrl: String
    private lateinit var ProfileImage : String
    private var post_number:Int = 0
    var currentPostNumber:Int=0

    private var progressDialog: ProgressDialog? = null
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
            showprogressbar()
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

//    private fun saveImageToFirebaseStorage(imageUri: Uri,Post_Number : Int) {
//
//    }


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

//    private fun savePostToDatabase(imageUri: Uri,post_number : Int) {
//        val databaseReference = FirebaseDatabase.getInstance().reference.child("posts")
//        // Create a SimpleDateFormat instance with the desired format
//
//
//        // Create a SimpleDateFormat instance with the desired format
//        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
//
//        // Get the current date
//
//        // Get the current date
//        val currentDate = Date()
//
//        // Format the current date using the SimpleDateFormat
//
//        // Format the current date using the SimpleDateFormat
//        val formattedDate = sdf.format(currentDate)
//
//        val storageRef = FirebaseStorage.getInstance().reference
//        val imageRef = storageRef.child("$useruid/post_pictures/$post_number.jpg")
//
//        imageRef.putFile(imageUri)
//            .addOnSuccessListener { taskSnapshot ->
//                // Image uploaded successfully
//                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
//                    val imageUrl = downloadUri.toString()
//
//                  //  databaseReference.child(useruid).child("profileImageUrl").setValue(imageUrl)
//
//                    val postMap = HashMap<String, Any>()
//
//                    postMap["username"] = binding.userName.text.toString()
//                    postMap["ProfileImage"] = ProfileImage
//                    postMap["post_number"] = post_number
//                    postMap["content"] = binding.caption.text.toString()
//                    postMap["location"] = binding.locationTxt.text.toString()
//                    postMap["imageUrl"] = imageUrl
//                    postMap["Post_Date"] = formattedDate// Use the profile image URL
//
//                    databaseReference.child(useruid).child(post_number.toString()).setValue(postMap)
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                Log.d("postNumber1",post_number.toString());
//                                AddPostNumber(post_number)
//                                dismissprogressbar()
//                                Toast.makeText(requireContext(), "Post saved successfully", Toast.LENGTH_SHORT).show()
//                            } else {
//                                dismissprogressbar()
//                                Toast.makeText(requireContext(), "Error saving post", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//
//                    if (isAdded) { // Check if the fragment is still attached
//                        // Display a success message if needed
//                        dismissprogressbar()
//                        Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//            .addOnFailureListener { e ->
//                if (isAdded) { // Check if the fragment is still attached
//                    // Handle image upload failure
//                    Toast.makeText(requireContext(), "Image upload failed: $e", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//
//
//    }


    private fun savePostToDatabase(imageUri: Uri, post_number: Int) {
        // ...

                val databaseReference = FirebaseDatabase.getInstance().reference.child("posts")
        // Create a SimpleDateFormat instance with the desired format


        // Create a SimpleDateFormat instance with the desired format
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)

        // Get the current date

        // Get the current date
        val currentDate = Date()

        val formattedDate = sdf.format(currentDate)

                val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("$useruid/post_pictures/$post_number.jpg")

        lifecycleScope.launch(Dispatchers.IO) { // Run in the IO dispatcher for network and disk I/O
            try {
                val taskSnapshot = imageRef.putFile(imageUri).await()
                val downloadUri = imageRef.downloadUrl.await()
                val imageUrl = downloadUri.toString()

                Log.d("Kan","here1")
                val postMap = HashMap<String, Any>()
                postMap["username"] = binding.userName.text.toString()
                postMap["ProfileImage"] = ProfileImage
                postMap["post_number"] = post_number
                postMap["content"] = binding.caption.text.toString()
                postMap["location"] = binding.locationTxt.text.toString()
                postMap["imageUrl"] = imageUrl
                postMap["Post_Date"] = formattedDate

                databaseReference.child(useruid).child(post_number.toString()).setValue(postMap)
                    .addOnCompleteListener { task ->

                        Log.d("Kan","here2")

                        if (task.isSuccessful) {
                            Log.d("postNumber1", post_number.toString())
                            AddPostNumber(post_number)
                            dismissprogressbar()
                            Toast.makeText(requireContext(), "Post saved successfully", Toast.LENGTH_SHORT).show()



                            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                            transaction.replace(R.id.container, PostFragment())
                            transaction.commit()

                        } else {
                            dismissprogressbar()
                            Toast.makeText(requireContext(), "Error saving post", Toast.LENGTH_SHORT).show()
                        }
                    }

                withContext(Dispatchers.Main) {
                    Log.d("Kan","here5")

                        Log.d("Kan","here3")
                        // UI-related operations if needed after the task completes
                        dismissprogressbar()
                        Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show()

                }
            } catch (e: Exception) {
                Log.d("Kan","here4")
                withContext(Dispatchers.Main) {

                        Toast.makeText(requireContext(), "Image upload failed: $e", Toast.LENGTH_SHORT).show()

                }
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
                currentPostNumber += 1
                savePostToDatabase(imageUrl.toUri(),currentPostNumber)
                Log.d("postNumber",currentPostNumber.toString());
            }else{
                currentPostNumber += 1
                savePostToDatabase(imageUrl.toUri(),currentPostNumber)
            }
        }.addOnFailureListener {

        }



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



    private fun showprogressbar(){
        progressDialog = ProgressDialog(requireContext())
        progressDialog?.setMessage("Finishing Up...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()

    }
    private fun dismissprogressbar(){
        progressDialog?.dismiss()
    }


}