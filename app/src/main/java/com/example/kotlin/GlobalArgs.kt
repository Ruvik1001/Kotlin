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

    val TaskTableName = "Job"

    val TaskFiled = listOf<Pair<String,String>>(
        Pair("login", "TEXT"),
        Pair("lable", "TEXT"),
        Pair("task_text", "TEXT"),
        Pair("date_from", "TEXT"),
        Pair("date_to", "TEXT"),
        Pair("status", "TEXT"),
    )

    val key1 = 0x8A
    val key2 = 0x3
}
