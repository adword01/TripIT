package com.example.tripit.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripit.Post
import com.example.tripit.adapters.PostAdapter
import com.example.tripit.R
import com.example.tripit.databinding.FragmentPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding

    private val postsData: MutableList<HashMap<String, Any>> = ArrayList()

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var useruid: String

    private var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=  FragmentPostBinding.inflate(inflater,container,false)

        firebaseAuth = FirebaseAuth.getInstance()

        useruid = firebaseAuth.currentUser?.uid.toString()
        binding.postRv.layoutManager = LinearLayoutManager(context)

        retrievePostsFromDatabase()

        binding.addNew.setOnClickListener {

            val createPostFragment = CreatePostFragment()

            // Get the FragmentManager and start a FragmentTransaction
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()

            // Replace the current fragment with the "Create Post" fragment
            transaction.replace(R.id.container, createPostFragment)
             transaction.addToBackStack(null)

            // Commit the transaction
            transaction.commit()
        }

        return binding.root
    }

    private fun retrievePostsFromDatabase() {
        val PostD = mutableListOf<Post>()
        val databaseReference = FirebaseDatabase.getInstance().reference.child("posts")
        postsData.clear()
        PostD.clear()

        databaseReference.get().addOnSuccessListener {dataSnapshot ->


            for (UserSnapshot in dataSnapshot.children){
                for (postSnapshot in UserSnapshot.children) {
                    val PostData = postSnapshot.getValue(Post::class.java)
                    PostData?.let {
                        PostD.add(0,it)
                    }

                    Log.d("Post1",PostD.toString())

                }

                if (PostD.isNullOrEmpty()){

                }
                else{
                    // Create and set the adapter with the retrieved data

                    binding.ProgressBar.visibility = View.GONE
                    val adapter = PostAdapter(PostD)
                    binding.postRv.adapter = adapter
                }
            }


        }
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//
//                for (UserSnapshot in dataSnapshot.children){
//                    for (postSnapshot in UserSnapshot.children) {
//                        val PostData = postSnapshot.getValue(Post::class.java)
//                        PostData?.let {
//                            PostD.add(0,it)
//                        }
//
//                        Log.d("Post",PostD.toString())
//
//                    }
//
//                    if (PostD.isNullOrEmpty()){
//
//                    }
//                    else{
//                        // Create and set the adapter with the retrieved data
//
//                        binding.ProgressBar.visibility = View.GONE
//                        val adapter = PostAdapter(PostD)
//                        binding.postRv.adapter = adapter
//                    }
//                }
//
//
//
//
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle errors here
//            }
//        })
    }


    private fun showprogressbar(){
        progressDialog = ProgressDialog(requireContext())
        progressDialog?.setMessage("Fetching Posts...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()

    }
    private fun dismissprogressbar(){
        progressDialog?.dismiss()
    }


}