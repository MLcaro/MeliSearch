package com.example.melisearch.ui.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melisearch.R
import com.example.melisearch.data.model.Category
import com.example.melisearch.data.model.CategoryTopList
import com.example.melisearch.data.model.ItemDetail
import com.example.melisearch.data.model.ItemDetailList
import com.example.melisearch.data.network.ApiClient
import com.example.melisearch.data.service.RetrofitHelper.getRetrofit
import com.example.melisearch.databinding.ActivityMainBinding
import com.example.melisearch.ui.view.adapter.ListAdapter
import com.example.melisearch.util.TextParameters
import com.example.melisearch.util.Utilities
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    private var list=mutableSetOf<ItemDetail>()
    private var favlist= mutableSetOf<ItemDetail>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerTop.layoutManager = LinearLayoutManager(this)
        val input :String=binding.textFieldInput.text.toString()
        categorySearchOnChange(input)
        clickSearchCategory()
        favlist=getList()
        onFavoritesLinkClick()
    }


    /**
     * function that receive a list of ItemDetail obj and creates and adapter to show in recyclerView
     * */
    private fun chargeRecyclerTopSearch(list :MutableSet<ItemDetail>, categoryName: String){
                val adapter = ListAdapter(list, favlist,
                        listener = {
                            if (it.id != null) {
                                startItemDetailActivity2(it.id)
                            }
                         },
                        listener2 = { addFavItem(favlist)})

        //add the adapter to the recyclerView space
        binding.recyclerTop.adapter = adapter
        //charge the category name
        binding.CategoryNameTv.text=categoryName
        binding.CategoryNameTv.visibility=View.VISIBLE
    }


    /**
     * on every click to the input text, search call for a new api response
     * */
    private fun clickSearchCategory (){
        binding.textFieldInput.setOnClickListener{
            categorySearch()
        }
    }

    /**function that show a recyclerView with the favorites selected products*/
    private fun onFavoritesLinkClick(){
        binding.showFav.setOnClickListener {
            val categoryName: String = ""
            binding.topItems.text="Tus Favoritos"
            favlist = getList()
            chargeRecyclerTopSearch(favlist, categoryName)
        }
    }
    /**
     * Function that add a list of items to the sharedPreferences key favorites*/
    private fun addFavItem(favList :MutableSet<ItemDetail>): Unit{
        setList("favorites", favList)
    }
    /**
     * Function that parse a list of ItemDetail obj in a json string and save it in sharedPreferences*/
    fun  setList(key: String?, list: MutableSet<ItemDetail>?) {
        val sharedPreferences :SharedPreferences = this.getSharedPreferences("PREFS", AppCompatActivity.MODE_PRIVATE)
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
        val sharedPreferences :SharedPreferences = this.getSharedPreferences("PREFS", AppCompatActivity.MODE_PRIVATE)
        val serializedObject = sharedPreferences.getString("favorites", null)
        if (serializedObject != null) {
            val gson = Gson()
            val type = object : TypeToken<MutableSet<ItemDetail?>?>() {}.type
            arrayItems = gson.fromJson<MutableSet<ItemDetail>>(serializedObject, type)
        }
        return arrayItems
    }

    /**
     * function that validate the input and call the function to get a server response
     * */
    private fun categorySearch() {
        val input :String=binding.textFieldInput.text.toString()
        if (Utilities.checkInput(input)) {
            getResponse(input)
        }else{
            showSnackBar(binding.recyclerTop, TextParameters.ERROR_NOT_VALID_INPUT.value)
        }
    }
    /**function that recall api on new create state when input text search is valid*/
    private fun categorySearchOnChange(input :String){
        if(Utilities.checkInput(input)){
            getResponse(input)
        }
    }
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
        showLoadingProgressBar(true)
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
                    val categoryName=category.category_name
                    val categoryId = category.category_id
                    val callTopItems: Response<CategoryTopList> =
                        getRetrofit().create(ApiClient::class.java).getTopItems(categoryId)
                        if(callTopItems.isSuccessful)
                        { val res2 = callTopItems.body() as CategoryTopList
                            val items = res2.content
                            Log.d("ITEMS ", items.toString())
                            if (items.size>0){
                                val itemIdlist = mutableListOf<String>()
                                items.forEach { if(it.type=="ITEM") itemIdlist.add(it.id) }
                                val itemsPath =itemIdlist.toString().replace("[", "").replace("]", "")
                                val callItems: Response<List<ItemDetailList>> =
                                    getRetrofit().create(ApiClient::class.java).getItemsDetail(itemsPath)
                                if(callItems.isSuccessful){
                                    Log.d("Items ", callItems.toString())
                                    val res3 = callItems.body() as List<ItemDetailList>

                                    val itemsDetailList: MutableSet<ItemDetail> = mutableSetOf()
                                        res3.forEach { itemsDetailList.add(it.itemDetail) }
                                    Log.d("items detail",itemsDetailList.toString())
                                    runOnUiThread {
                                        showLoadingProgressBar(false)
                                        list=itemsDetailList
                                        chargeRecyclerTopSearch(list, categoryName)
                                    }
                                }else{
                                    runOnUiThread {
                                        showError()
                                        Log.d(
                                            "SERVER ERROR MULTIGET CALL",
                                            callItems.code().toString()
                                        )
                                    }
                                }
                            }

                        }else{
                            runOnUiThread {
                                showError()
                                Log.d("SERVER ERROR TOP ITEMS CALL", callTopItems.code().toString())
                            }
                        }
                }else{
                    runOnUiThread {
                        showError()
                        Log.d("NO DATA", "Not a valid category id received")
                    }
                }
            } else {
                runOnUiThread {
                    showError()
                    Log.d("SERVER ERROR CATEGORY CALL", callCategory.code().toString())
                }
            }
        }

    }

    /**
     * function that show a message to the app user when is not a successful response of the search
     * */
    private fun showError(){
        showLoadingProgressBar(false)
        list.clear()
        binding.recyclerTop.adapter?.notifyDataSetChanged()
        binding.CategoryNameTv.visibility=View.INVISIBLE
        showSnackBar(binding.recyclerTop, TextParameters.ERROR_CATEGORY_NOT_FOUND.value)
    }


    //UI IMPROVEMENTS
    private fun showSnackBar(view: View, message: String) {
        val snack = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snack.show()
    }

    private fun showLoadingProgressBar(show: Boolean) {
        val loading = findViewById<ProgressBar>(R.id.loadingPB)
        if (show) {
            loading.visibility = View.VISIBLE
        } else {
            loading.visibility = View.INVISIBLE
        }
    }


}