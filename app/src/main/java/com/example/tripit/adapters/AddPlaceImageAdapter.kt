package com.example.tripit.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripit.R
import com.squareup.picasso.Picasso

class AddPlaceImageAdapter(private val imageUris: List<Uri>) : RecyclerView.Adapter<AddPlaceImageAdapter.AddImageViewHolder>() {

    class AddImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_place_image, parent, false)
//        return ImageViewHolder(view)
//    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_grid, parent, false)
        return AddImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddImageViewHolder, position: Int) {
        Picasso.get().load(imageUris[position]).into(holder.imageView)
    }

    override fun getItemCount(): Int = imageUris.size
}
