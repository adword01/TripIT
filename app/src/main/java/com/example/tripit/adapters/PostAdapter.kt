package com.example.tripit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripit.Post
import com.example.tripit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.user_name)
        val Location: TextView = itemView.findViewById(R.id.location)
        val UserProfile: ImageView = itemView.findViewById(R.id.user_profile_img)
        val like: ImageView = itemView.findViewById(R.id.like_counter)
        val PostImage : ImageView = itemView.findViewById(R.id.PostImageView)
        val caption : TextView = itemView.findViewById(R.id.caption)
        val like_counter : TextView = itemView.findViewById(R.id.like_counter_no)
        val Date : TextView = itemView.findViewById(R.id.timing)
        // Add more views for other post data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        val postRef = FirebaseDatabase.getInstance()
            .getReference("posts")
            .child(post.uid)
            .child(post.post_number.toString())
        holder.like_counter.text = post.likeCount().toString()
        holder.usernameTextView.text = post.username
        holder.Location.text = post.location
        holder.caption.text = post.content
        holder.Date.text = "Posted On: ${ post.Post_Date }"

        if (post.isLikedBy(currentUserId!!)) {
            holder.like.setImageResource(R.drawable.filled_heart)
        } else {
            holder.like.setImageResource(R.drawable.unfilled_heart)
        }
        // Load images into ImageView using Picasso
        val imageUrl = post.ProfileImage
        Picasso.get().load(imageUrl).into(holder.UserProfile)
        val PostUrl = post.imageUrl
        Picasso.get().load(PostUrl).into(holder.PostImage)

        holder.like.setOnClickListener {
            postRef.child("likes").child(currentUserId).get().addOnSuccessListener {
                if (it.exists()) {
                    // Unlike
                    postRef.child("likes").child(currentUserId).removeValue()
                } else {
                    // Like
                    postRef.child("likes").child(currentUserId).setValue(true)
                }
            }
        }

        // Real-time update listener
        postRef.child("likes").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val likeCount = snapshot.childrenCount
                holder.like_counter.text = likeCount.toString()
                if (snapshot.hasChild(currentUserId)) {
                    holder.like.setImageResource(R.drawable.filled_heart)
                } else {
                    holder.like.setImageResource(R.drawable.unfilled_heart)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        // Set other post data
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}

