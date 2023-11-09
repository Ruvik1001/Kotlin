package com.example.kotlin.windows.special

import retrofit2.Retrofit

const val foodTableName = "Food"
const val drinksTableName = "Drink"

val productFiled = listOf<Pair<String,String>>(
    Pair("position", "TEXT"),
    Pair("cost", "TEXT"),
    Pair("count", "TEXT"),
    Pair("image", "TEXT")
)

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://text-host.ru")
    .build()