package com.example.melisearch.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.melisearch.R
import com.example.melisearch.data.model.ItemDetail

class ListAdapter(private val itemsList: List<ItemDetail>, val listenerer: (ItemDetail) -> Unit) :
    RecyclerView.Adapter<ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListViewHolder(layoutInflater.inflate(R.layout.item_top, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val itemPosition = itemsList[position]
        holder.binder(itemPosition)
        holder.itemView.setOnClickListener {listenerer(itemPosition)}
    }

    override fun getItemCount(): Int = itemsList.size
}