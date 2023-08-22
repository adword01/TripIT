package com.example.tripit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ItineraryAdapter(private val items: List<String>) : RecyclerView.Adapter<ItineraryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itinerary_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position == 0)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemTitle: TextView = itemView.findViewById(R.id.textView19)
        private val itemcard: CardView = itemView.findViewById(R.id.itnerary_card)

        fun bind(item: String, isFirstItem: Boolean) {
            itemTitle.text = item

            if (isFirstItem) {
                // Set color1 for the first item
                itemcard.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.itinerary_selected_bg))
            } else {
                // Set color2 for all other items
                itemcard.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.itinerary_unselected_bg))
            }

            // Bind other views with item details if needed
        }
    }
}

