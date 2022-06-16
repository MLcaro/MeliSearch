package com.example.melisearch.data.model

import com.google.gson.annotations.SerializedName

class CategoryTopList (
    @SerializedName("query_data")val id:QueryData,
    @SerializedName("content") val content : ArrayList<Item>) {

}