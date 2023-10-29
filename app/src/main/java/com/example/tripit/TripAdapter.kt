package com.example.tripit

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TripAdapter(private val data: List<String>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<TripAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trip_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        // Bind data to the views in the list item layout
        holder.tripNameTextView.text = item
//        holder.tripExpenditureTextView.text = item.tripExpenditure
        getTotalExpense(item.toString(),holder.tripExpenditureTextView)

        // Set an onClickListener to make the item clickable
        holder.cardView.setOnClickListener {
            listener.onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val tripNameTextView: TextView = itemView.findViewById(R.id.tripName)
        val tripExpenditureTextView: TextView = itemView.findViewById(R.id.tripExpenditure)
    }

    interface OnItemClickListener {
        fun onItemClick(item: String)
    }
    private fun getTotalExpense(TripName : String,Textview : TextView){
        val reference = FirebaseDatabase.getInstance().reference.child("Trips").child(TripName)



        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {



                if (snapshot.exists()){
                    for (dataSnapShot in snapshot.children){
                        val valueText = dataSnapShot.value.toString()
                        val String = dataSnapShot.key

                        if (String == "Total_Price" ){

                            val value = valueText.toDouble()

                            Textview.text = "Total Expense is $value"
                        }



                    }
                }


            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event if needed
            }
        })
    }

}