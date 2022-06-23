package com.example.melisearch.ui.view.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.melisearch.data.model.ItemDetail
import com.example.melisearch.databinding.ItemTopBinding
import com.squareup.picasso.Picasso


class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemTopBinding.bind(view)
    /**
     * function to map a view with a given itemDetail Object*/
    fun binder(item: ItemDetail) {

        binding.itemTittleTV.text= item.title
        binding.itemDetailTV.text=item.site_id
        binding.itemPriceTV.text="$ "+item.price.toString()
        Picasso.get().load(item.thumbnail).into(binding.itemTopImage)







    }

}