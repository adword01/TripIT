package com.example.tripit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter : RecyclerView.Adapter<ExpenseAdapter.TripViewHolder>() {
    private var trips: List<ExpenseDetail> = emptyList()

    inner class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val PersonNameTextview: TextView = itemView.findViewById(R.id.NameTextView)
        val PurposeTextview: TextView = itemView.findViewById(R.id.PurposeTextView)
        val PriceTextview: TextView = itemView.findViewById(R.id.PriceTextView)

        fun bind(trip: ExpenseDetail) {
                PersonNameTextview.text = trip.personName

                    PurposeTextview.text = trip.purpose

                    PriceTextview.append(trip.price.toString())


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_trip, parent, false)
        return TripViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind(trips[position])
    }

    override fun getItemCount(): Int {
        return trips.size
    }

    fun setTrips(trips: List<ExpenseDetail>) {
        this.trips = trips
        notifyDataSetChanged()
    }
}


