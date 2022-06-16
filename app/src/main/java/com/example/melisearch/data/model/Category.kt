package com.example.melisearch.data.model

data class Category (val domain_id: String,
val domain_name : String ,
val category_id : String,
val category_name: String,
val attributes: List<String>){
}