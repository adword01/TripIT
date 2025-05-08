package com.example.tripit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripit.R
import com.example.tripit.dataclasses.PackingListItem

class PackingListAdapter(
    private val items: List<PackingListItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_LOCATION = 0
        private const val TYPE_CALENDAR = 1
        private const val TYPE_TRAVEL_TYPE = 2
        private const val TYPE_MODAL = 3
        private const val TYPE_LUGGAGE = 4
        private const val TYPE_ACCOMMODATION = 5
        private const val TYPE_SUMMARY = 6
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is PackingListItem.LocationItem -> TYPE_LOCATION
            is PackingListItem.CalendarItem -> TYPE_CALENDAR
            is PackingListItem.TravelType -> TYPE_TRAVEL_TYPE
            is PackingListItem.TravelModal -> TYPE_MODAL
            is PackingListItem.TravelLuggage -> TYPE_LUGGAGE
            is PackingListItem.TravelAccomodation -> TYPE_ACCOMMODATION
            is PackingListItem.TravelSummary -> TYPE_SUMMARY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_LOCATION -> LocationViewHolder(inflater.inflate(R.layout.item_location_pkl, parent, false))
            TYPE_CALENDAR -> CalendarViewHolder(inflater.inflate(R.layout.item_calender_pkl, parent, false))
            TYPE_TRAVEL_TYPE -> TravelTypeViewHolder(inflater.inflate(R.layout.item_travel_reason_pkl, parent, false))
            TYPE_MODAL -> ModalViewHolder(inflater.inflate(R.layout.item_modal_pkl, parent, false))
            TYPE_LUGGAGE -> LuggageViewHolder(inflater.inflate(R.layout.item_luggage_pkl, parent, false))
            TYPE_ACCOMMODATION -> AccomodationViewHolder(inflater.inflate(R.layout.item_accomodation_pkl, parent, false))
            TYPE_SUMMARY -> SummaryViewHolder(inflater.inflate(R.layout.item_summary_pkl, parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is PackingListItem.LocationItem -> (holder as LocationViewHolder).bind(item)
            is PackingListItem.CalendarItem -> (holder as CalendarViewHolder).bind(item)
            is PackingListItem.TravelType -> (holder as TravelTypeViewHolder).bind(item)
            is PackingListItem.TravelModal -> (holder as ModalViewHolder).bind(item)
            is PackingListItem.TravelLuggage -> (holder as LuggageViewHolder).bind(item)
            is PackingListItem.TravelAccomodation -> (holder as AccomodationViewHolder).bind(item)
            is PackingListItem.TravelSummary -> (holder as SummaryViewHolder).bind(item)
        }
    }

    inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val etSearch: EditText = itemView.findViewById(R.id.etLocationSearch)
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.rvLocationResults)

        private val dummyLocations = listOf("Una", "Manali", "Shimla", "Dharamshala", "Kullu")

        fun bind(item: PackingListItem.LocationItem) {
            recyclerView.layoutManager = LinearLayoutManager(itemView.context)
            val adapter = LocationResultAdapter(dummyLocations)
            recyclerView.adapter = adapter

            etSearch.addTextChangedListener {
                val query = it.toString()
                val filtered = dummyLocations.filter { loc -> loc.contains(query, ignoreCase = true) }
                adapter.updateList(filtered)
            }
        }
    }

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val calendarView: CalendarView = itemView.findViewById(R.id.calendarView)

        fun bind(item: PackingListItem.CalendarItem) {
            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                Toast.makeText(itemView.context, "Selected: $selectedDate", Toast.LENGTH_SHORT).show()
                // You can store this date in ViewModel or callback
            }
        }
    }

    inner class TravelTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PackingListItem.TravelType) {
            Toast.makeText(itemView.context, "Selected:", Toast.LENGTH_SHORT).show()

            // Bind UI like ChipGroup for travel purpose
        }
    }

    inner class ModalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PackingListItem.TravelModal) {
            // Bind transport mode UI
        }
    }

    inner class LuggageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PackingListItem.TravelLuggage) {
            // Bind luggage info UI
        }
    }

    inner class AccomodationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PackingListItem.TravelAccomodation) {
            // Bind accommodation type UI
        }
    }

    inner class SummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PackingListItem.TravelSummary) {
            // Bind trip summary UI
        }
    }
}
