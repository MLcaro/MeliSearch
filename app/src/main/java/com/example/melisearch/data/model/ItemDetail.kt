package com.example.melisearch.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ItemDetail(
    @SerializedName("id") val id: String?,//MLM853062906,
    @SerializedName("site_id") val site_id: String?,// "MLM",
    @SerializedName("title") val title: String?,//"Set 4 Bolsas Moderna Mujer Dama Cuero Sintetico Hombro",
    @SerializedName("subtitle") val subtitle: String?,// null,
    @SerializedName("seller_id") val seller_id:Int, // 626364401,
    @SerializedName("category_id") val category_id: String?, // "MLM5537",
    @SerializedName("official_store_id") val official_store_id: Int, // 4312,
    @SerializedName("price") val price:Float, // 213.49,
    @SerializedName("base_price") val base_price:Float,// 213.49,
    @SerializedName("original_price") val original_price:Float, // 245.16,
    @SerializedName("currency_id") val currency_id:String?,// "MXN",
    @SerializedName("initial_quantity") val initial_quantity:Int,// 29422,
    @SerializedName("available_quantity") val available_quantity:Int, // 1552,
    @SerializedName("sold_quantity") val sold_quantity:Int,// 16750,
    @SerializedName("secure_thumbnail") val thumbnail: String?,
    @SerializedName("pictures") val pictures:List<PictureItemDetail>

    ) {

}