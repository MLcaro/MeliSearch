package com.example.melisearch.ui.view

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
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class ItemDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val itemSelected=intent.getSerializableExtra("itemId").toString()
        getItem(itemSelected)
        //createItem()
    }

    private fun createItem(itemSelected: ItemDetail){
        if(itemSelected!=null){

            binding.secondaryTitleTV.text=itemSelected.subtitle?:"Sin Subtitulo"
            binding.primaryTitleTV.text=itemSelected.title
            binding.detailPriceTV.text="$ "+itemSelected.price.toString()
            binding.secondaryTitleTV.text=itemSelected.domain.lowercase()
            Picasso.get().load(itemSelected.pictures[0].url).into(binding.itemImages)
        }


    }

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
                        createItem(item)
                    }
                }  else{
                    Log.d("SERVER ERROR", callItems.code().toString())
                }
            }
    }
}