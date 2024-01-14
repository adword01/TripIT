package com.example.tripit.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tripit.databinding.FragmentProfileBinding
import com.example.tripit.fragments.CreatePostFragment.Companion.REQUEST_IMAGE_PICK
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var useruid:String
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var profileImageURL: String
    private val PREFS_KEY_IMAGE_URL = "profile_image_url"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)



        binding.profilePic.setOnClickListener {
            pickImageFromGallery()
        }

        val preferences = requireContext().getSharedPreferences("MyPrefs", MODE_PRIVATE)
        profileImageURL = preferences.getString(PREFS_KEY_IMAGE_URL, "") ?: ""

        if (profileImageURL.isNotEmpty()) {
            // If a profile image URL is available, load the image into the ImageView
            Picasso.get().load(profileImageURL).into(binding.profilePic)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        useruid=firebaseAuth.currentUser?.uid.toString()

        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        checkProfileImageUrlInDatabase()


        return binding.root
    }

    private fun pickImageFromGallery() {
        if (ContextCompat.checkSelfPermission(
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
            val imageUri: Uri = data.data!!
            val imagePath = getRealPathFromURI(imageUri)

            // Display the selected image using Picasso
            Picasso.get().load(imageUri).into(binding.profilePic)

            // Save the image to Firebase Database along with the download URL
            saveImageToFirebaseStorage(imageUri)
        }
    }
    private fun saveImageToFirebaseStorage(imageUri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("profile_pictures/$useruid.jpg")

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()

                    // Save the image URL to the user's database entry
                    databaseReference.child(useruid).child("profileImageUrl").setValue(imageUrl)

//                    if (isAdded) { // Check if the fragment is still attached
//                        // Save the image URL to SharedPreferences for future use
//                        saveImageUrlToSharedPreferences(imageUrl)
//                    }

                    if (isAdded) { // Check if the fragment is still attached
                        // Display a success message if needed
                        Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener { e ->
                if (isAdded) { // Check if the fragment is still attached
                    // Handle image upload failure
                    Toast.makeText(requireContext(), "Image upload failed: $e", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkProfileImageUrlInDatabase() {
        // Check if the profile image URL exists in the database
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        databaseReference.child(useruid).child("profileImageUrl")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SuspiciousIndentation")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val imageUrl = dataSnapshot.value.toString()
                        // If a profile image URL is available in the database, load the image
                        Picasso.get().load(imageUrl).into(binding.profilePic)

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors here
                }
            })
    }


    private fun getRealPathFromURI(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activity?.contentResolver?.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val path = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return path ?: ""
    }
}