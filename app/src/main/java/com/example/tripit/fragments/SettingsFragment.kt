package com.example.tripit.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.example.tripit.LoginActivity
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

    private lateinit var sharedPreferences: SharedPreferences

    private var newStatus: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentSettingsBinding.inflate(layoutInflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("NotificationPrefs", Context.MODE_PRIVATE)
        val isNotificationEnabled = sharedPreferences.getBoolean("notification_enabled", false)
        updateNotificationIcon(isNotificationEnabled)

        binding.progressBar.visibility = View.VISIBLE

        binding.profile.setOnClickListener {
            loadFragment(ProfileFragment())
        }

        binding.linearLayout4.setOnClickListener {
            openRatingBar()
        }

        binding.linearLayout.setOnClickListener {
            // Toggle the notification status and update the icon
            val isNotificationEnabled = sharedPreferences.getBoolean("notification_enabled", false)
            newStatus = !isNotificationEnabled
            notificationDisplay()
        }

        binding.linearLayout7.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(),LoginActivity::class.java)
            startActivity(intent)
        }

        checkProfileImageUrlInDatabase()

        return binding.root
    }

    private fun notificationDisplay() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_lottie)

        val animationView: LottieAnimationView = dialog.findViewById(R.id.lottieAnimationView)

        // Set the Lottie animation file you want to play
        animationView.setAnimation(R.raw.notification)

        // Add an animation listener to close the dialog when the animation finishes
        animationView.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                updateNotificationIcon(newStatus)
                dialog.dismiss()
            }
        })

        dialog.setCancelable(false) // Prevent the user from dismissing the dialog manually
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
    }

    private fun openRatingBar() {
        val ratingDialog = RatingDialogFragment()
        ratingDialog.show(requireActivity().supportFragmentManager, "RatingDialogFragment")
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
    private fun updateNotificationIcon(isNotificationEnabled: Boolean) {
        val notificationIcon: ImageView = binding.notifyImg

        if (isNotificationEnabled) {
            notificationIcon.setImageResource(R.drawable.bell_filled)
        } else {
            notificationIcon.setImageResource(R.drawable.bell)
        }

        // Save the updated notification status in SharedPreferences
        sharedPreferences.edit().putBoolean("notification_enabled", isNotificationEnabled).apply()
    }
}