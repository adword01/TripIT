package com.example.tripit.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.tripit.PersonDataManager
import com.example.tripit.R

class PersonAdapter(private val people: List<String>, private val dataManager: PersonDataManager, val PersonName : String) :
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    inner class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sf = itemView.context.getSharedPreferences("PricePrefs",Context.MODE_PRIVATE)
              val string = sf.getString("PriceTab",null)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val checkBoxEditText: EditText = itemView.findViewById(R.id.PriceEditext)


        fun bind(person: String) {
            checkBox.text = person

            //checkBox.isChecked = dataManager.isPersonChecked(person)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                dataManager.setPersonChecked(person, isChecked)

            }
            checkBoxEditText.visibility=View.GONE

            checkBoxEditText.visibility = if (string.isNullOrBlank() || string == "false") View.GONE else View.VISIBLE

            checkBoxEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    dataManager.setPersonPrice(person, s.toString())
                }
            })

//            checkBoxEditText.setOnFocusChangeListener { _, hasFocus ->
//                if (!hasFocus) {
//                    val price = checkBoxEditText.text.toString()
//                    dataManager.setPersonPrice(person, price)
//                }
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_checkbox, parent, false)
        return PersonViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {

            holder.bind(people[position])


    }

    override fun getItemCount(): Int {
        return people.size
    }
}




//class PersonAdapter(private val people: List<String>) :
//    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {
//
//
//
//    inner class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//                val sf = itemView.context.getSharedPreferences("PricePrefs",Context.MODE_PRIVATE)
//        val string = sf.getString("PriceTab",null)
//        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
//        val checkBoxEditText: EditText = itemView.findViewById(R.id.PriceEditext)
//
//        fun bind(person: String) {
//            checkBox.text = person
//            checkBox.isChecked = personCheckBoxState[person] == true
//
//            checkBox.setOnCheckedChangeListener { _, isChecked ->
//                personCheckBoxState[person] = isChecked
//            }
//
//            checkBoxEditText.visibility = if (string.isNullOrBlank() || string == "false") View.GONE else View.VISIBLE
//
//            checkBoxEditText.setOnFocusChangeListener { _, hasFocus ->
//                if (!hasFocus) {
//                    val price = checkBoxEditText.text.toString()
//                    personPrices[person] = price
//                }
//            }
//        }
//    }
//
//
////    inner class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
////        val sf = itemView.context.getSharedPreferences("PricePrefs",Context.MODE_PRIVATE)
////        val string = sf.getString("PriceTab",null)
////        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
////        val checkBoxEditText: EditText = itemView.findViewById(R.id.PriceEditext)
////
////        fun bind(person: String) {
////            checkBox.text = person
////
////            checkBox.setOnCheckedChangeListener { _, isChecked ->
////                personCheckBoxState[person] = isChecked
////                Log.d("adapter21",isChecked.toString())
////            }
////            Log.d("adapter22",person + personCheckBoxState.toString())
////
////
////
////
////            checkBoxEditText.visibility = if (string.isNullOrBlank() || string == "false") View.GONE else View.VISIBLE
////
////
////            checkBoxEditText.setOnFocusChangeListener { _, hasFocus ->
////                if (!hasFocus) {
////                    val price = checkBoxEditText.text.toString()
////                    personPrices[person] = price
////                }
////            }
////
////
////
////            // Track the price entered in the EditText
//////            checkBoxEditText.addTextChangedListener(object : TextWatcher {
//////                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//////
//////                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//////
//////                override fun afterTextChanged(s: Editable?) {
//////                    personPrices[person] = s.toString()
//////                }
//////            })
////        }
////
////
//////        fun bind(person: String) {
//////
//////            checkBox.text = person
//////
//////            checkBox.isChecked = personCheckBoxState[person] == true
//////
//////            checkBox.setOnCheckedChangeListener { _, isChecked ->
//////                // Update the checkbox state when it's checked or unchecked
//////                personCheckBoxState[person] = isChecked
//////            }
//////
//////            if (string.isNullOrBlank() || string.equals("false")){
//////
//////                checkBoxEditText.visibility = View.GONE
//////            }else{
////////                checkBox.text = person
//////                checkBoxEditText.visibility = View.VISIBLE
//////            }
//////
//////
//////
//////        }
////    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_checkbox, parent, false)
//        return PersonViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
//        holder.bind(people[position])
//
//
//
//    }
//
//    override fun getItemCount(): Int {
//        return people.size
//    }
//
//
//    val personCheckBoxState = mutableMapOf<String, Boolean>()
//    val personPrices = mutableMapOf<String, String>()
//
//
//    fun getClickedPeople(): Map<String, String> {
//        val selectedPeopleWithPrices = mutableMapOf<String, String>()
//
//        for ((personName, isChecked) in personCheckBoxState) {
//            if (isChecked) {
//                val price = personPrices[personName] ?: ""
//                selectedPeopleWithPrices[personName] = price
//                Log.d("adapter2",price.toString())
//            }
//        }
//        Log.d("adapter1",selectedPeopleWithPrices.toString())
//
//        return selectedPeopleWithPrices
//    }
//
//
////    fun getClickedPeople(): List<String> {
////        val clickedPeople = mutableListOf<String>()
////
////        for ((personName, isChecked) in personCheckBoxState) {
////            if (isChecked) {
////                clickedPeople.add(personName)
////            }
////        }
////
////        return clickedPeople
////    }
//
//
////    private fun PushPersonData(context: Context,person: String){
////        val sf = context.getSharedPreferences("TripPrefs", Context.MODE_PRIVATE)
////        val tripName = sf.getString("TripName",null)
////        val reference = FirebaseDatabase.getInstance().reference.child("Trips").child(tripName.toString())
////
////        reference.child("Expense").child(binding.namePerson.text.toString()).push().setValue( hashMapOf(
////            "Purpose" to binding.ExpenseTitle.text.toString(),
////            "Price" to binding.Price.text.toString(),
////            "Name" to binding.namePerson.text.toString()
////        )).addOnSuccessListener {
////
////
////
////
////
////
////
////
////            reference.addValueEventListener(object: ValueEventListener {
////                override fun onDataChange(snapshot: DataSnapshot) {
////                    val priceText = binding.Price.text.toString()
////
////
////
////                    if (snapshot.exists()){
////                        for (dataSnapShot in snapshot.children){
////                            val valueText = dataSnapShot.value.toString()
////                            val String = dataSnapShot.key
////
////                            if (String == "Total_Price" && !priceText.isNullOrEmpty() ){
////
////                                Log.d("Kanishk2", valueText)
////                                val price = priceText.toDouble()
////                                val value = valueText.toDouble()
////
////                                val total = price + value
////                                Log.d("Expense", "Yes $total")
////                                reference.child("Total_Price").setValue(total.toString())
////                                binding.namePerson.text.clear()
////                                binding.ExpenseTitle.text?.clear()
////                                binding.Price.text?.clear()
////                            }
////
////
////
////                        }
////                    }
////
////
////                }
////
////                override fun onCancelled(error: DatabaseError) {
////                    // Handle onCancelled event if needed
////                }
////            })
////
////
////
////            Toast.makeText(requireContext(),"Added Successfully", Toast.LENGTH_SHORT).show()
////        }
////
////    }
//}
