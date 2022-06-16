package com.example.melisearch.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.melisearch.R
import com.example.melisearch.data.model.Category
import com.example.melisearch.data.model.CategoryTopList
import com.example.melisearch.data.model.ItemDetail
import com.example.melisearch.data.model.ItemDetailList
import com.example.melisearch.data.network.ApiClient
import com.example.melisearch.data.service.RetrofitHelper.getRetrofit
import com.example.melisearch.databinding.ActivityMainBinding
import com.example.melisearch.ui.view.adapter.ListAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var list: List<ItemDetail>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerTop.layoutManager = LinearLayoutManager(this)
        searchCategory()
    }

    /**
     * function that receive a list of ItemDetail obj and creates and adapter to show in recyclerView
     * */
    private fun chargeRecyclerTopSearch(list :List<ItemDetail>){
        val adapter = ListAdapter(list) {
            //startItemDetailActivity(it)
            if(it.id!=null){
                startItemDetailActivity2(it.id)
            }

        }
        //add the adapter to the recyclerView space
        binding.recyclerTop.adapter = adapter
    }

    /**
     * on every click to the input text, search call for a new api response
     * */
    private fun searchCategory (){
        binding.textFieldInput
            .setOnClickListener {
                getResponse(binding.textFieldInput.text.toString())
        }
    }
    /**
     * function that start the detail activity of the selected item
     *
    private fun startItemDetailActivity ( item :ItemDetail){
        val detailIntent = Intent(this@MainActivity,ItemDetailActivity::class.java)
        detailIntent.putExtra("item", item)
        startActivity(detailIntent)
    }*/

    /**
     * function that start the detail activity of the selected item
     * */
    private fun startItemDetailActivity2 ( itemId:String){
        val detailIntent = Intent(this@MainActivity,ItemDetailActivity::class.java)
        detailIntent.putExtra("itemId", itemId)
        startActivity(detailIntent)
    }

    /**
     * Function that calls 3 endpoint of an api to get the most sales items of a category related to the input search,
     * if it doesn't find a valid category or an items set, return a message to the user
     * */
    private fun getResponse(input :String) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("SERVER", input)
            val callCategory: Response<List<Category>> =
                getRetrofit().create(ApiClient::class.java).getCategoryCall(
                    1,
                    input.lowercase())
            if (callCategory.isSuccessful) {
                Log.d("SERVER Success", callCategory.body().toString())
                val response = callCategory.body()?: emptyList()
                if(response.isNotEmpty()) {
                    val category = callCategory.body()?.get(0) as Category
                    val categoryId = category.category_id
                    val callTopItems: Response<CategoryTopList> =
                        getRetrofit().create(ApiClient::class.java).getTopItems(categoryId)
                        if(callTopItems.isSuccessful)
                        {
                            val res2 = callTopItems.body() as CategoryTopList
                            val items = res2.content
                            if (items.size>0){
                                //TODO lista debe ser filtrada por tipo y ordenada por position
                                val itemsId = items.toList().map { "${it.id}" }
                                Log.d("items : ", itemsId.toString())
                                //TODO cambiar por metodo con regex
                                val itemsIdString=itemsId.toString().replace(" ", "")
                                val items1 =itemsIdString.replace("[","")
                                val items2 =items1.replace("]","")
                                val callItems: Response<List<ItemDetailList>> =
                                    getRetrofit().create(ApiClient::class.java).getItemsDetail(items2)
                                if(callItems.isSuccessful){
                                    val res3 = callItems.body() as List<ItemDetailList>
                                    //get a list of itemDetails
                                    val itemsDetailList: List<ItemDetail> = res3.map { it.itemDetail }
                                    Log.d("items detail",itemsDetailList.toString())
                                    runOnUiThread {
                                        chargeRecyclerTopSearch(itemsDetailList)
                                    }
                                }else{
                                    showError()
                                    Log.d("SERVER ERROR MULTIGET CALL", callItems.code().toString())
                                }
                            }

                        }else{
                            showError()
                            Log.d("SERVER ERROR TOP ITEMS CALL", callTopItems.code().toString())
                        }
                }else{
                    showError()
                    Log.d("NO DATA", "Not a valid category id received")
                }
            } else {
                showError()
                Log.d("SERVER ERROR CATEGORY CALL", callCategory.code().toString())

            }
        }

    }

    /**
     * function that show a message to the app user when is not a successful response of the search
     * */
    private fun showError(){
        showSnackBar(binding.recyclerTop, "No category or product was found for your request, try another word ;)")
    }

    private fun showSnackBar(view: View, message: String) {
        val snack = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snack.show()
    }

    fun String.removeWhitespaces() = replace(" ", "")

}