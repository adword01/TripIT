package com.example.tripit.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tripit.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import java.io.IOException


class CreatePostFragment : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var databaseReference: DatabaseReference

    companion object {
        const val REQUEST_IMAGE_PICK = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =
            inflater.inflate(com.example.tripit.R.layout.fragment_create_post, container, false)

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("posts")

        imageView = view.findViewById<ImageView>(com.example.tripit.R.id.imageView11)

        imageView.setOnClickListener {
            pickImageFromGallery()
        }

        return view
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

            Picasso.get().load(imageUri).into(imageView)

            try {
                // Extract location information from image metadata
                val exif = ExifInterface(imagePath)
                val latLong = FloatArray(2)
                if (exif.getLatLong(latLong)) {
                    val latitude = latLong[0].toDouble()
                    val longitude = latLong[1].toDouble()

                    // Save the location to Firebase
                    saveLocationToFirebase(imageUri.toString(), latitude, longitude)
                } else {

                    val location_lyt= view?.findViewById<LinearLayout>(R.id.location_lyt)
                    location_lyt?.visibility=View.VISIBLE

                    
//                    Toast.makeText(requireContext(),"No coordinates",Toast.LENGTH_SHORT).show()

                    // Handle the case where the image does not contain GPS coordinates.
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
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

    private fun saveLocationToFirebase(imageUrl: String, latitude: Double, longitude: Double) {
        // Create a HashMap to store location information
        val locationData = HashMap<String, Any>()
        locationData["latitude"] = latitude
        locationData["longitude"] = longitude
        locationData["imageUrl"] = imageUrl

        // Push the data to Firebase
        val postId = databaseReference.push().key
        if (postId != null) {
            databaseReference.child(postId).setValue(locationData)
            Toast.makeText(requireContext(),"Done",Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(requireContext(),"Empty",Toast.LENGTH_SHORT).show()

    }
}