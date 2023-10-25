package com.example.tripit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class PostAdapter(private val posts: List<HashMap<String, Any>>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.user_name)
        val postImageView: ImageView = itemView.findViewById(R.id.user_profile_img)
        // Add more views for other post data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.usernameTextView.text = post["username"].toString()

        // Load images into ImageView using Picasso
        val imageUrl = post["imageUrl"].toString()
        Picasso.get().load(imageUrl).into(holder.postImageView)

        // Set other post data
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}

