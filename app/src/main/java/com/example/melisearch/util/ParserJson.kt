package com.example.melisearch.util

import android.content.SharedPreferences
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.melisearch.data.model.ItemDetail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ParserJson {
    companion object{
        /**
         * function that gets the shared preferences key favorites and parse the data to an ItemDetail object Mutable set*/
        /*fun getList(context : View): MutableSet<ItemDetail> {
            var arrayItems=mutableSetOf<ItemDetail>()
            val sharedPreferences : SharedPreferences = context.getSharedPreferences("PREFS", AppCompatActivity.MODE_PRIVATE)
            val serializedObject = sharedPreferences.getString("favorites", null)
            if (serializedObject != null) {
                val gson = Gson()
                val type = object : TypeToken<MutableSet<ItemDetail?>?>() {}.type
                arrayItems = gson.fromJson<MutableSet<ItemDetail>>(serializedObject, type)
            }
            return arrayItems
        }*/

    }
}