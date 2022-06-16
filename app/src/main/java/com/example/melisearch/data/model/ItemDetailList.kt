package com.example.melisearch.data.model

import com.google.gson.annotations.SerializedName

data class ItemDetailList (
    @SerializedName("code")val code:String,
    @SerializedName("body") val itemDetail : ItemDetail
        ){
}