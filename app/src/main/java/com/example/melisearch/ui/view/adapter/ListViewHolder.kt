package com.example.melisearch.ui.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.melisearch.data.model.ItemDetail
import com.example.melisearch.databinding.ItemTopBinding
import com.squareup.picasso.Picasso



class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemTopBinding.bind(view)

    fun binder(item: ItemDetail) {
        val shortTitle=item.title?.substring(0,25)
        binding.itemTittleTV.text= shortTitle
        binding.itemDetailTV.text=item.subtitle
        binding.itemPriceTV.text=item.price.toString()
        Picasso.get().load(item.thumbnail).into(binding.itemTopImage)
        binding.icon.setOnCheckedChangeListener { checkBox, isChecked ->
            // Respond to icon toggle
        }
    }

}