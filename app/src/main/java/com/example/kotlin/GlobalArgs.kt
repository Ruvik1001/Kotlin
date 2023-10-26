package com.example.kotlin

class GlobalArgs {
    val UserTableName = "Users"
    val UserFiled = listOf<Pair<String,String>>(
        Pair("login", "TEXT"),
        Pair("password", "TEXT"),
        Pair("name", "TEXT"),
        Pair("last", "TEXT"),
        Pair("patronic", "TEXT"),
        Pair("post", "TEXT"),
        Pair("phone", "TEXT"),
    )
}
