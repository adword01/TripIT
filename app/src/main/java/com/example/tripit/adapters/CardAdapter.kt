package com.example.tripit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tripit.databinding.CardItemBinding
import com.example.tripit.viewmodels.Card

class CardAdapter(private val items: List<Card>) : RecyclerView.Adapter<CardViewHolder>() {
    init {
        setHasStableIds(true)
    }

    override fun getItemCount() = items.size
    override fun getItemId(pos: Int) = items[pos].hashCode().toLong()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CardViewHolder(CardItemBinding.inflate(parent.layoutInflater, parent, false))

    override fun onBindViewHolder(holder: CardViewHolder, pos: Int) {
        holder.bind(items[pos])
    }
}


private val ViewGroup.layoutInflater: LayoutInflater get() = LayoutInflater.from(context)
