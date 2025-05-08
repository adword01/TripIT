package com.example.tripit.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripit.R
import com.example.tripit.adapters.TripAdapter
import com.example.tripit.databinding.FragmentExpenditureBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date


class ExpenditureFragment : Fragment() {


    private lateinit var binding: FragmentExpenditureBinding
    var numberOfEntries: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExpenditureBinding.inflate(inflater,container,false)

        binding.TripRecyclerview.layoutManager = LinearLayoutManager(context) // Set your desired layout manager


        getPersons()
        getData()

        binding.SaveTripBtn.setOnClickListener {
            saveQuizToRealtimeDatabase(numberOfEntries)
            binding.PeopleScoll.visibility = View.GONE
        }
        binding.showPackingListTxt.visibility = View.GONE
        binding.showPackingListTxt.setOnClickListener {
            val PackingListFragment = PackingListFragment()

            // Get the FragmentManager and start a FragmentTransaction
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()

            // Replace the current fragment with the "Create Post" fragment
            transaction.replace(R.id.container, PackingListFragment)
            transaction.addToBackStack(null)

            // Commit the transaction
            transaction.commit()
        }

        return binding.root
    }

    private fun getData(){

        val sf = requireContext().getSharedPreferences("TripPrefs",Context.MODE_PRIVATE)
        // Firebase reference to your "Trips" node
        val databaseReference = FirebaseDatabase.getInstance().getReference().child("Trips").child(FirebaseAuth.getInstance().uid.toString())

// Create a list to store the keys
        val tripKeys = mutableListOf<String>()

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    // The key is obtained using getKey() method
                    val key = snapshot.key
                    key?.let {
                        tripKeys.add(it)
                    }
                }

                val adapter = TripAdapter(tripKeys,object : TripAdapter.OnItemClickListener{
                    override fun onItemClick(item: String) {
                        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                        val transaction: FragmentTransaction = fragmentManager.beginTransaction()


                        // Replace the current fragment with the "Create Post" fragment
                        transaction.replace(R.id.container, TripExpenseFragment())
                        transaction.addToBackStack(null)
                        sf.edit().putString("TripName",item).apply()
                        // Commit the transaction
                        transaction.commit()

                        Toast.makeText(requireContext(),item,Toast.LENGTH_SHORT).show()
                    }

                })
                binding.TripRecyclerview.adapter = adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error if necessary
            }
        }
        databaseReference.addListenerForSingleValueEvent(valueEventListener)
    }

    private fun getPersons() {
        val container: LinearLayout = binding.editTextContainer
        val generateButton: Button = binding.generateButton

        generateButton.setOnClickListener {
            binding.PeopleScoll.visibility = View.VISIBLE
            val numberOfEntriesField: EditText = binding.numberOfEntries
            numberOfEntries = numberOfEntriesField.text.toString().toIntOrNull()
                ?: 0 // Convert input to integer, default to 0 if not a valid number
            container.removeAllViews() // Clear any existing views in the container
            for (i in 1..numberOfEntries) {
                val view = layoutInflater.inflate(R.layout.edit_text_set, null)
                container.addView(view)
                val questionNumberTextView = view.findViewById<TextView>(R.id.questionNumberTextView)
                questionNumberTextView.text = "Person $i"


            }

        }

    }



    private fun saveQuizToRealtimeDatabase(numberOfQuestions: Int) {
        val database = Firebase.database
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val title = dateFormat.format(Date())

        val questions = mutableMapOf<String, Any>()
        for (i in 1..numberOfQuestions) {
            val view = binding.editTextContainer.getChildAt(i - 1)

            val description = view.findViewById<EditText>(R.id.description).text.toString().trim()

            if (description.isEmpty() ) {
                // Show error message and return without saving
                Toast.makeText(
                    requireContext(),
                    "Please fill name of person $i",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            questions["person$i"] = description
        }

//        val quiz = hashMapOf(
//            "questions" to questions
//        )

        val databaseRef = database.getReference("Trips").child(FirebaseAuth.getInstance().uid.toString()).child(binding.TripTitle.text.toString()).child("Persons")
        databaseRef.setValue(questions)
        binding.TripTitle.text?.clear()
        binding.numberOfEntries.text?.clear()


        Toast.makeText(requireContext(), "Trip Saved", Toast.LENGTH_SHORT).show()

    }

}