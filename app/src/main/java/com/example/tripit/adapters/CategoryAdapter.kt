package com.example.tripit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripit.R
import com.example.tripit.viewmodels.onCategoryClicked

class CategoryAdapter(private val catList: List<Int>,
                      private val catName: List<String>,
                        private val category : onCategoryClicked) :
    RecyclerView.Adapter<CategoryAdapter.CatViewHolder>() {

    class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.cat_image)
        val imageName: TextView = itemView.findViewById(R.id.cat_name)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_grid, parent, false)
        return CatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.imageView.setImageResource(catList[position])
        holder.imageName.text = catName[position]
        holder.itemView.setOnClickListener {
            category.onCategoryClicked(catName[position])
        }
    }

    override fun getItemCount(): Int {
        return catList.size
    }
}