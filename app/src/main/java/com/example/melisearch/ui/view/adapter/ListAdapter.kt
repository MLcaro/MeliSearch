package com.example.melisearch.ui.view.adapter

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.melisearch.R
import com.example.melisearch.data.model.ItemDetail
import com.example.melisearch.util.PorterDuffColors

class ListAdapter(private val itemsList: MutableSet<ItemDetail>, private val favoritesList:MutableSet<ItemDetail>,
                  val listener2: (MutableSet<ItemDetail>)->Unit, val listener:(ItemDetail) -> Unit) : RecyclerView.Adapter<ListViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListViewHolder(layoutInflater.inflate(R.layout.item_top, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val itemPosition = itemsList.toList()[position]
        holder.binder(itemPosition)
        holder.itemView.setOnClickListener {listener(itemPosition)}
        if(favoritesList.contains(itemPosition)){
            holder.binding.fabBtn.colorFilter =PorterDuffColors.RED.value
        }
        holder.binding.fabBtn.setOnClickListener {
            listener2(favAdd(holder, itemPosition,favoritesList))

        }
    }

    override fun getItemCount(): Int = itemsList.size
}
/**
 * Function that change the color of the fav icon and add the item selected to a shared preferences favorites list*/
private fun favAdd(holder:ListViewHolder,item: ItemDetail, favList: MutableSet<ItemDetail>) : MutableSet<ItemDetail> {
    val buttonColor=holder.binding.fabBtn.drawable.colorFilter
    if (buttonColor==PorterDuffColors.RED.value){
        favList.remove(item)
        holder.binding.fabBtn.colorFilter = PorterDuffColors.GREY.value
    }else{
        favList.add(item)
        holder.binding.fabBtn.colorFilter = PorterDuffColors.RED.value

    }
    return favList
}