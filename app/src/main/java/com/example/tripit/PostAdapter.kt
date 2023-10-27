package com.example.tripit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.user_name)
        val Location: TextView = itemView.findViewById(R.id.location)
        val UserProfile: ImageView = itemView.findViewById(R.id.user_profile_img)
        val PostImage : ImageView = itemView.findViewById(R.id.PostImageView)
        val caption : TextView = itemView.findViewById(R.id.caption)
        val Date : TextView = itemView.findViewById(R.id.timing)
        // Add more views for other post data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.usernameTextView.text = post.username
        holder.Location.text = post.location
        holder.caption.text = post.content
        holder.Date.text = "Posted On: ${ post.Post_Date }"

        // Load images into ImageView using Picasso
        val imageUrl = post.ProfileImage
        Picasso.get().load(imageUrl).into(holder.UserProfile)
        val PostUrl = post.imageUrl
        Picasso.get().load(PostUrl).into(holder.PostImage)


        // Set other post data
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}

