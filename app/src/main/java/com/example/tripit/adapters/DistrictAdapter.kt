package com.example.tripit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripit.District
import com.example.tripit.R

class DistrictAdapter( private var trips: List<District>): RecyclerView.Adapter<DistrictAdapter.TripViewHolder>() {
   // private var trips: List<District> = emptyList()
   private var onItemClickListener: OnItemClickListener? = null

    fun submitList(newList: List<District>) {
        trips = newList
        notifyDataSetChanged()
    }

    // Interface for item click listener
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }


    inner class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val DistrictImage: ImageView = itemView.findViewById(R.id.districtimg)
        val Name: TextView = itemView.findViewById(R.id.DistrictText)
        val layout : LinearLayout = itemView.findViewById(R.id.layoutItem)



        fun bind(trip: District) {
            DistrictImage.setBackgroundResource(trip.imageResourceId)

            Name.text = trip.purpose

//            layout.setOnClickListener {
//
//
//
//                loadFragment(DistrictFragment())
//
//                val editor = itemView.context.getSharedPreferences("DistrictPref", Context.MODE_PRIVATE).edit()
//                editor.putString("district", trip.purpose)
//                editor.apply()
//
//            }

            itemView.setOnClickListener(View.OnClickListener {
                onItemClickListener?.onItemClick(position)
            })

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.districtitem, parent, false)
        return TripViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trips = trips[position]
      //  val districtName = districtNames[position]

        holder.bind(trips)
       // holder.bind(trips[position])
    }

    override fun getItemCount(): Int {
        return trips.size
    }

    fun setTrips(trips: List<District>) {
        this.trips = trips
        notifyDataSetChanged()
    }

//    private fun loadFragment(fragment: Fragment) {
//        val transaction = HomeActivity().supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.container,fragment)
//        transaction.commit()
//        transaction.addToBackStack(null)
//    }
}