package com.example.melisearch.data.model

import com.google.gson.annotations.SerializedName

class QueryData (
    @SerializedName("highlight_type") val highlight: String,//"BEST_SELLER",
    @SerializedName("criteria")val criteria:String,// "CATEGORY",
    @SerializedName("id") val id: String,// "MLM5537"
        ) {
}