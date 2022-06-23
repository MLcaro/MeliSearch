package com.example.melisearch.ui.view

import android.content.SharedPreferences
import android.graphics.Color.BLUE
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.melisearch.R
import com.example.melisearch.data.model.Category
import com.example.melisearch.data.model.ItemDetail
import com.example.melisearch.data.model.ItemDetailList
import com.example.melisearch.data.network.ApiClient
import com.example.melisearch.data.service.RetrofitHelper
import com.example.melisearch.databinding.ActivityItemDetailBinding
import com.example.melisearch.databinding.ActivityMainBinding
import com.example.melisearch.util.PorterDuffColors
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class ItemDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemDetailBinding
    private var favlist= mutableSetOf<ItemDetail>()
    lateinit var actualItem:ItemDetail
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val itemSelected=intent.getSerializableExtra("itemId").toString()
        getItem(itemSelected)
        onClickAddFavBt()
        onClickAddFavIcon()
        //createItem()
    }
    /**
     * Function that creates the UI for an item detail, receives an ItemDetail obj*/
    private fun createItem(itemSelected: ItemDetail){
        if(itemSelected!=null){

            binding.secondaryTitleTV.text=itemSelected.subtitle?:"Sin Subtitulo"
            binding.primaryTitleTV.text=itemSelected.title
            binding.detailPriceTV.text="$ "+itemSelected.price.toString()
            binding.secondaryTitleTV.text=itemSelected.domain?.lowercase()
            Picasso.get().load(itemSelected.pictures[0].url).into(binding.itemImages)
        }


    }

    /**
     * Function that parse a list of ItemDetail obj in a json string and save it in sharedPreferences*/
    fun  setList(key: String?, list: MutableSet<ItemDetail>?) {
        val sharedPreferences : SharedPreferences = this.getSharedPreferences("PREFS", AppCompatActivity.MODE_PRIVATE)
        val  editor : SharedPreferences.Editor  = sharedPreferences.edit();
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(key, json)
        editor.commit()

    }

    /**
     * function that gets the shared preferences key favorites and parse the data to an ItemDetail object Mutable set*/
    fun getList(): MutableSet<ItemDetail> {
        var arrayItems=mutableSetOf<ItemDetail>()
        val sharedPreferences : SharedPreferences = this.getSharedPreferences("PREFS", AppCompatActivity.MODE_PRIVATE)
        val serializedObject = sharedPreferences.getString("favorites", null)
        if (serializedObject != null) {
            val gson = Gson()
            val type = object : TypeToken<MutableSet<ItemDetail?>?>() {}.type
            arrayItems = gson.fromJson<MutableSet<ItemDetail>>(serializedObject, type)
        }
        return arrayItems
    }
    /**OnClick functions to add an item to favorite list*/
    private fun onClickAddFavBt(){
        binding.detailFavAddBt.setOnClickListener {
            println("COLOR "+binding.detailFavAddBt.currentTextColor)
            val red =-65536
            val regularColor =-12291382
            if(binding.detailFavAddBt.currentTextColor==regularColor){
                binding.detailFavAddBt.setTextColor(red)
                onClickAddFav()
            }else{
                binding.detailFavAddBt.setTextColor(regularColor)
                onClickRemoveFav()
            }

        }
    }

    private fun onClickAddFavIcon(){
        binding.detailFavIcon.setOnClickListener {
            if(binding.detailFavIcon.colorFilter!=PorterDuffColors.RED.value) {
                binding.detailFavIcon.colorFilter = PorterDuffColors.RED.value
                onClickAddFav()
            }else{
                binding.detailFavIcon.colorFilter = PorterDuffColors.GREY.value
                onClickRemoveFav()
            }
        }
    }
    /***
     * Function to add the actual item to the favorites list
     */
    private fun onClickAddFav(){
        favlist=getList()
        favlist.add(actualItem)
        setList("favorites", favlist )
    }

    private fun onClickRemoveFav(){
        favlist=getList()
        favlist.remove(actualItem)
        setList("favorites", favlist )
    }

    /**Function that make an api call to get the item detail*/
    private fun getItem(id: String){
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("ID :", id)
                val callItems: Response<List<ItemDetailList>> =
                    RetrofitHelper.getRetrofit().create(ApiClient::class.java).getItemsDetail(id)
                if (callItems.isSuccessful) {
                    val res3 = callItems.body() as List<ItemDetailList>
                    val itemsDetailList: ItemDetailList = res3[0];
                    val item : ItemDetail=itemsDetailList.itemDetail
                    runOnUiThread {
                        actualItem=item
                        createItem(item)
                    }
                }  else{
                    Log.d("SERVER ERROR", callItems.code().toString())
                }
            }
    }
}