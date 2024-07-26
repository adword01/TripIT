package com.example.tripit.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripit.adapters.ExpenseAdapter
import com.example.tripit.ExpenseDetail
import com.example.tripit.adapters.PersonAdapter
import com.example.tripit.PersonDataManager
import com.example.tripit.R
import com.example.tripit.databinding.FragmentTripExpenseBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class TripExpenseFragment : Fragment() {

    private val personDataManager = PersonDataManager()

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



//        binding.AddButton.setOnClickListener {
//            if (binding.ExpenseTitle.text.isNullOrBlank() || binding.Price.text.isNullOrBlank() || binding.namePerson.text.isNullOrBlank()) {
//            }else{
//               // pushData(tripName.toString())
//            }
//
//        }

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
        val expenseTextView = dialogView.findViewById<TextView>(R.id.expenseTextView)
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
            val reference = FirebaseDatabase.getInstance().reference.child("Trips").child(FirebaseAuth.getInstance().uid.toString()).child(TripName).child("Expense").child(namePerson.text.toString())


            pgbar.visibility = View.VISIBLE

            reference.get().addOnSuccessListener {
                val expenseDetails = mutableListOf<ExpenseDetail>()
                if (it.exists()){
                    val DataSnapshot = it.children

                    DataSnapshot.forEach { DataSnapshot->

                        val DetailsSnapshot = DataSnapshot.child("Users")

                        if (DetailsSnapshot.exists()){
                            for (NewSnapshot in DetailsSnapshot.children){
                                Log.d("ExpenseDetails",NewSnapshot.key.toString())

                                    val personName = NewSnapshot.child("Name").value.toString()
                                    val priceStr = NewSnapshot.child("Price").value.toString()

                                    val price = if (priceStr != "null" && !priceStr.isNullOrBlank()) {
                                        priceStr.toDouble()

                                    } else {
                                        // Handle the case when priceStr is null or "null"
                                        0.0 // or any default value you prefer
                                    }
                                    val expense = ExpenseDetail(personName,"",price)
                            expenseDetails.add(expense)

                                if (expenseDetails.isNotEmpty()){
                            Log.d("ExpenseDetails",expenseDetails.toString())
                        }else{
                            Log.d("ExpenseDetails","Kanishk")
                        }
                            }

                        }

                    }


                }

                if(expenseDetails.isNotEmpty()){
                    val expenseMap = expenseDetails.groupBy(ExpenseDetail::personName)
                    val finalList = expenseMap.map { (personName, expenseDetails) ->
                        val totalExpense = expenseDetails.sumByDouble { it.price }
                        ExpenseDetail(personName, "", totalExpense)
                    }



                    val displayText = finalList.joinToString("   ") {
                        "${it.personName} amount ${it.price}"
                    }

                    expenseTextView.text = displayText

//                    for (expense in finalList) {
//                        println("Person: ${expense.personName}, Total Expense: ${expense.price}")
//                    }
                }


            }




            reference.addListenerForSingleValueEvent(object : ValueEventListener {
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
    fun PushIndividualPrice(context: Context,TripName: String,namesList: List<String>) {
        // Create a custom view for the AlertDialog
        val inflater = LayoutInflater.from(context)
        val dialogView: View = inflater.inflate(R.layout.add_expense_layout, null)


        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.PersonCheckBoxrecyclerview)
        val namePerson = dialogView.findViewById<AutoCompleteTextView>(R.id.namePerson)
        val SaveBtn = dialogView.findViewById<Button>(R.id.SaveExpenseBtn)
        val TabLayout = dialogView.findViewById<TabLayout>(R.id.TabLayout)
        val Price = dialogView.findViewById<TextInputEditText>(R.id.Price)
        val Puropose = dialogView.findViewById<TextInputEditText>(R.id.ExpenseTitle)


        val schooladapter = ArrayAdapter(requireContext(),R.layout.item_list,namesList)
        namePerson.setAdapter(schooladapter)
        var Name = ""

        namePerson.setOnItemClickListener { parent, view, position, id ->
            // Get the selected item (in this case, a String)
            val selectedItem = parent.adapter.getItem(position) as String
            val newList =  namesList.toMutableList()
            newList.remove(selectedItem)
            Name = selectedItem
            val adapter = PersonAdapter(newList,personDataManager,selectedItem)
            recyclerView.adapter = adapter
        }

       // val TotalExpenseTextView = dialogView.findViewById<TextView>(R.id.TotalExpenseTextView)
        recyclerView.layoutManager = LinearLayoutManager(context)
       // val pgbar = dialogView.findViewById<ProgressBar>(R.id.pgbar)

        val sf  = context.getSharedPreferences("PricePrefs",Context.MODE_PRIVATE)
        val editor = sf.edit()
        val personName  = namePerson.text.toString()



       TabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // Handle tab selection, and update the fragment content accordingly
                when (tab.position) {
                    0 -> {
                        val newList = namesList.toMutableList()
                        newList.remove(Name)
                        editor.putString("PriceTab","false").apply()
                        val adapter = PersonAdapter(newList,personDataManager,personName)
                        recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                    1 -> {
                        val newList = namesList.toMutableList()
                        newList.remove(Name)
                        editor.putString("PriceTab", "true").apply()
                        val adapter = PersonAdapter(newList,personDataManager,personName)
                        recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Handle tab unselection if needed
                editor.putString("PriceTab","false").apply()
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Handle tab reselection if needed
            }
        })

        Log.d("Tab",sf.getString("PriceTab","k").toString())


        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)













      //  val closeButton = dialogView.findViewById<ImageView>(R.id.closeButton)


        val dialog = builder.create()
        dialog.show()

        SaveBtn.setOnClickListener {

            pushData(dialog,namePerson.text.toString(),Price,Puropose.text.toString(),TripName)

        }

        dialog.setOnDismissListener {
            // Set your string to null when the dialog is dismissed
            editor.putString("PriceTab", null).apply()
        }

//        closeButton.setOnClickListener {
//
//            dialog.dismiss()
//        }

    }





    private fun pushData(dialog: AlertDialog,namPerson : String,Price: TextInputEditText,Puropose : String,TripName : String){

        val reference = FirebaseDatabase.getInstance().reference.child("Trips").child(FirebaseAuth.getInstance().uid.toString()).child(TripName)


       // val selectedPeople = adapter.getClickedPeople()

        val personDataMap = HashMap<String, Any>() // Create a HashMap for all the selected eople
        val selectedPeopleWithPrices = personDataManager.getSelectedPeopleWithPrices()
        Log.d("adapter",selectedPeopleWithPrices.toString())

// You can now use selectedPeopleWithPrices in your fragment
        for ((personName, price) in selectedPeopleWithPrices) {
            if (personName != namPerson && !price.equals("")){
                val expenseData = hashMapOf(
                    "Price" to price,
                    "Name" to personName
                )

                personDataMap[personName] = expenseData
            }else if(personName != namPerson && price.equals("")){

                val NewPrice =  Price.text.toString().toDouble() / (selectedPeopleWithPrices.size +1)
                val expenseData = hashMapOf(
                    "Price" to NewPrice,
                    "Name" to personName
                )

                personDataMap[personName] = expenseData
            }


        }

            val newExpenseNode = reference.child("Expense").child(namPerson).push()
            val expenseData = hashMapOf(
                "Purpose" to Puropose,
                "Price" to Price.text.toString(),
                "Name" to namPerson,
                "Users" to personDataMap
            )

            newExpenseNode.setValue(expenseData).addOnSuccessListener {
                dialog.dismiss()

                reference.get().addOnSuccessListener {
                    val priceText = Price.text.toString()
                    if (it.exists()){
                        val dataSnapShot = it.child("Total_Price")
                        if (dataSnapShot.exists()){

                            val price = priceText.toDouble()
                            val value = dataSnapShot.value.toString().toDouble()

                            val total = price + value
                            Log.d("Expense", "Yes $total")
                            reference.child("Total_Price").setValue(total.toString())
                            Toast.makeText(requireContext(),"Added Successfully",Toast.LENGTH_SHORT).show()

                        }else{
                            val price = priceText.toDouble()
                            reference.child("Total_Price").setValue(price.toString())
                            Toast.makeText(requireContext(),"Added Successfully",Toast.LENGTH_SHORT).show()
                        }
                        Log.d("Kanishk2", String.toString())
                    }
                }


//                reference.addValueEventListener(object: ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        val priceText = Price.text.toString()
//
//
//
//                        if (snapshot.exists()){
//                            for (dataSnapShot in snapshot.children){
//                                val valueText = dataSnapShot.value.toString()
//                                val String = dataSnapShot.key.toString()
//
//                                Log.d("Kanishk2", String.toString())
//                                if (String == "Total_Price" && priceText.isNotEmpty() ){
//
//                                    Log.d("Kanishk2", valueText)
//                                    val price = priceText.toDouble()
//                                    val value = valueText.toDouble()
//
//                                    val total = price + value
//                                    Log.d("Expense", "Yes $total")
//                                    reference.child("Total_Price").setValue(total.toString())
//                                    Toast.makeText(requireContext(),"Added Successfully",Toast.LENGTH_SHORT).show()
//                                    return
////                                    binding.namePerson.text.clear()
////                                    binding.ExpenseTitle.text?.clear()
////                                    binding.Price.text?.clear()
//
//                                }
//                                else if (String != "Total_Price"){
//                                    val price = priceText.toDouble()
//                                    reference.child("Total_Price").setValue(price.toString())
//                                    Toast.makeText(requireContext(),"Added Successfully",Toast.LENGTH_SHORT).show()
//                                    return
//                                }
//
//
//
//                            }
//                        }
//
//
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        // Handle onCancelled event if needed
//                    }
//                })


            }



    }

    private fun fetchTripsFromFirebase(TripName: String) {
        // Replace 'YOUR_DATABASE_REF' with the actual Firebase Realtime Database reference
        val databaseRef = FirebaseDatabase.getInstance().reference.child("Trips").child(FirebaseAuth.getInstance().uid.toString()).child(TripName)

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val expenseDetails = mutableListOf<ExpenseDetail>()
                    for (expenseSnapshot in snapshot.child("Expense").children) {
                        val personName = expenseSnapshot.key


                        for (detailSnapshot in expenseSnapshot.children) {
                            val Key = detailSnapshot.key
                                val purpose = detailSnapshot.child("Purpose").value.toString()
                                val personName = detailSnapshot.child("Name").value.toString()
                                val priceStr = detailSnapshot.child("Price").value.toString()

                                val price = if (priceStr != "null" && !priceStr.isNullOrBlank()) {
                                    priceStr.toDouble()
                                } else {
                                    // Handle the case when priceStr is null or "null"
                                    0.0 // or any default value you prefer
                                }

                                val expenseDetail = ExpenseDetail(personName,purpose, price)
                                expenseDetails.add(expenseDetail)



                        }


                    }



                val adapter = ExpenseAdapter()
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

        val databaseReference = FirebaseDatabase.getInstance().getReference().child("Trips").child(FirebaseAuth.getInstance().uid.toString()).child(tripName.toString()).child("Persons")
        val namesList = mutableListOf<String>()
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        val key = snapshot.key
                        val value = snapshot.value
                        namesList.add(value.toString())
                    }
//                    val schooladapter = ArrayAdapter(requireContext(),R.layout.item_list,namesList)
//                    binding.namePerson.setAdapter(schooladapter)


                    binding.AddExpense.setOnClickListener {
                        PushIndividualPrice(requireContext(),tripName.toString(),namesList)
                    }

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