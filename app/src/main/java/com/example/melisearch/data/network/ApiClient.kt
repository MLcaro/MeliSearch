package com.example.melisearch.data.network

import com.example.melisearch.data.model.Category
import com.example.melisearch.data.model.CategoryTopList
import com.example.melisearch.data.model.ItemDetailList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
const val token = "APP_USR-6827585486363840-062223-d7aacbb12bbd6bd6a930eda7460767fb-1140056368"
interface ApiClient {

    /**
     * function to get a category from the input text*/
    @Headers("Authorization: Bearer $token")
    @GET("sites/MLM/domain_discovery/search?")
    suspend fun getCategoryCall(@Query("limit") limit: Int=1, @Query("q") query: String): Response<List<Category>>

    /**
     * function to get a category from the input text*/
    @Headers("Authorization: Bearer $token")
    @GET("highlights/MLM/category/{id}/")
    suspend fun getTopItems(@Path("id") id: String): Response<CategoryTopList>

    /**
     * function to get a category from the input text*/
    @Headers("Authorization: Bearer $token")
    @GET("items?")
    suspend fun getItemsDetail(@Query(value="ids", encoded = true) ids:String): Response<List<ItemDetailList>>
}