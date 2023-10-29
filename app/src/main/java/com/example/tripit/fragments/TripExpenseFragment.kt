package com.example.tripit.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripit.Expense
import com.example.tripit.ExpenseAdapter
import com.example.tripit.ExpenseDetail
import com.example.tripit.R
import com.example.tripit.Trip
import com.example.tripit.databinding.FragmentTripExpenseBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.checkerframework.checker.units.qual.s
import java.security.Key


class TripExpenseFragment : Fragment() {

    private lateinit var binding : FragmentTripExpenseBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTripExpenseBinding.inflate(inflater,container,false)

        val sf = requireContext().getSharedPreferences("TripPrefs", Context.MODE_PRIVATE)
        val tripName = sf.getString("TripName",null)
        binding.TripName.text = tripName

        getData()
        binding.ExpenseRecyclerview.layoutManager = LinearLayoutManager(context)



        binding.AddButton.setOnClickListener {
            pushData(tripName.toString())
        }

        fetchTripsFromFirebase(tripName.toString())
        return binding.root
    }

    @SuppressLint("MissingInflatedId")
    fun getIndividualData(context: Context,TripName: String,namesList: List<String>) {
        // Create a custom view for the AlertDialog
        val inflater = LayoutInflater.from(context)
        val dialogView: View = inflater.inflate(R.layout.individual_expense, null)

//        val titleText = dialogView.findViewById<TextView>(R.id.TitleTextView)
//        titleText.text = "Teachers"

        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyclerView)
        val namePerson = dialogView.findViewById<AutoCompleteTextView>(R.id.namePerson)
        val searchBtn = dialogView.findViewById<Button>(R.id.SearchBtn)
        val TotalExpenseTextView = dialogView.findViewById<TextView>(R.id.TotalExpenseTextView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val pgbar = dialogView.findViewById<ProgressBar>(R.id.pgbar)

        var totalPrice : Double = 0.0
        val builder = android.app.AlertDialog.Builder(context)
        builder.setView(dialogView)

        // getLiveClasses(studentClass,recyclerView)
        val schooladapter = ArrayAdapter(requireContext(),R.layout.item_list,namesList)
        namePerson.setAdapter(schooladapter)

        searchBtn.setOnClickListener {
            totalPrice = 0.0
            val studentsRef = FirebaseDatabase.getInstance().reference.child("Trips").child(TripName).child("Expense").child(namePerson.text.toString())


            pgbar.visibility = View.VISIBLE
            studentsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val expenseDetails = mutableListOf<ExpenseDetail>()


                    for (detailSnapshot in dataSnapshot.children) {
                        val purpose = detailSnapshot.child("Purpose").value.toString()
                        val personName = detailSnapshot.child("Name").value.toString()
                        val priceStr = detailSnapshot.child("Price").value.toString()

                        val price = if (priceStr != "null" && !priceStr.isNullOrBlank()) {
                            priceStr.toDouble()

                        } else {
                            // Handle the case when priceStr is null or "null"
                            0.0 // or any default value you prefer
                        }
                        totalPrice += price

                        val expenseDetail = ExpenseDetail(personName,purpose, price)
                        expenseDetails.add(expenseDetail)
                    }



                    if (expenseDetails.isNotEmpty()) {
                        pgbar.visibility = View.GONE
                        TotalExpenseTextView.text = "Total Expenditure: Rs$totalPrice"
                        val adapter = ExpenseAdapter()
                        adapter.setTrips(expenseDetails)
                        recyclerView.adapter = adapter
                        Log.d("Teacher", expenseDetails.toString())
                    } else {

                        pgbar.visibility = View.GONE
                        Log.d("student", "not found")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error, if any

                    pgbar.visibility = View.GONE
                    Log.d("student", "database error")
                }
            })
        }





      //  val closeButton = dialogView.findViewById<ImageView>(R.id.closeButton)


        val dialog = builder.create()
        dialog.show()

//        closeButton.setOnClickListener {
//
//            dialog.dismiss()
//        }

    }





    private fun pushData(TripName : String){

        val reference = FirebaseDatabase.getInstance().reference.child("Trips").child(TripName)

        reference.child("Expense").child(binding.namePerson.text.toString()).push().setValue( hashMapOf(
            "Purpose" to binding.ExpenseTitle.text.toString(),
            "Price" to binding.Price.text.toString(),
            "Name" to binding.namePerson.text.toString()
        )).addOnSuccessListener {



            reference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val priceText = binding.Price.text.toString()



                    if (snapshot.exists()){
                        for (dataSnapShot in snapshot.children){
                            val valueText = dataSnapShot.value.toString()
                            val String = dataSnapShot.key

                            if (String == "Total_Price" && !priceText.isNullOrEmpty() ){

                                    Log.d("Kanishk2", valueText)
                                    val price = priceText.toDouble()
                                    val value = valueText.toDouble()

                                    val total = price + value
                                    Log.d("Expense", "Yes $total")
                                    reference.child("Total_Price").setValue(total.toString())
                                    binding.namePerson.text.clear()
                                    binding.ExpenseTitle.text?.clear()
                                    binding.Price.text?.clear()
                                }



                        }
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled event if needed
                }
            })



            Toast.makeText(requireContext(),"Added Successfully",Toast.LENGTH_SHORT).show()
        }

    }

    private fun fetchTripsFromFirebase(TripName: String) {
        // Replace 'YOUR_DATABASE_REF' with the actual Firebase Realtime Database reference
        val databaseRef = FirebaseDatabase.getInstance().reference.child("Trips").child(TripName)

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tripsList = mutableListOf<Trip>()
                    val expenses = mutableListOf<Expense>()
                val expenseDetails = mutableListOf<ExpenseDetail>()
                    for (expenseSnapshot in snapshot.child("Expense").children) {
                        val personName = expenseSnapshot.key

                        Log.d("Person",personName.toString())

                        for (detailSnapshot in expenseSnapshot.children) {
                            val Key = detailSnapshot.key
                            Log.d("Person1",Key.toString())
                                val purpose = detailSnapshot.child("Purpose").value.toString()
                                val personName = detailSnapshot.child("Name").value.toString()
                                val priceStr = detailSnapshot.child("Price").value.toString()

                                val price = if (priceStr != "null" && !priceStr.isNullOrBlank()) {
                                    priceStr.toDouble()
                                } else {
                                    // Handle the case when priceStr is null or "null"
                                    0.0 // or any default value you prefer
                                }

                                Log.d("Person2",purpose + price)
                                val expenseDetail = ExpenseDetail(personName,purpose, price)
                                expenseDetails.add(expenseDetail)



                        }

                        val expense = Expense(personName.toString(), expenseDetails)
                        expenses.add(expense)
                    }

                    val trip = Trip(TripName, expenses)
                    tripsList.add(trip)

                val adapter = ExpenseAdapter()
                Log.d("Persom",tripsList.toString())
                adapter.setTrips(expenseDetails)
                binding.ExpenseRecyclerview.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
    }


    private fun getData(){
        val sf = requireContext().getSharedPreferences("TripPrefs", Context.MODE_PRIVATE)
        val tripName = sf.getString("TripName",null)

        val databaseReference = FirebaseDatabase.getInstance().getReference().child("Trips").child(tripName.toString()).child("Persons")
        val namesList = mutableListOf<String>()
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        val key = snapshot.key
                        val value = snapshot.value // This will give you the value stored at this key
                        // Process the key and value as needed
                        namesList.add(value.toString())
                    }
                    val schooladapter = ArrayAdapter(requireContext(),R.layout.item_list,namesList)
                    binding.namePerson.setAdapter(schooladapter)

                    binding.IndividualExpenseBtn.setOnClickListener {
                        getIndividualData(requireContext(),tripName.toString(),namesList)
                    }

                } else {
                    // The "Amritsar" node does not exist or is empty
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error if necessary
            }
        })


    }

}