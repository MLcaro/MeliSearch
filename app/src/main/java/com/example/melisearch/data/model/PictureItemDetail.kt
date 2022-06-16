package com.example.melisearch.data.model

import com.google.gson.annotations.SerializedName

data class PictureItemDetail (
    @SerializedName("id") val id:String ,// "992637-MLM43192648311_082020",
    @SerializedName("secure_url") val url: String,// "https://http2.mlstatic.com/D_992637-MLM43192648311_082020-O.jpg",
        ){
}