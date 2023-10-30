package com.example.kotlin.special.global

class GlobalArgs() {
    var mode: Int = 1

    var FROM_NOTIFICATION = "FROM_NOTIFICATION"

    val key1 = 0x8A
    val key2 = 0x3

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

    val AboutTableName = "About"

    val AboutFiled = listOf<Pair<String,String>>(
        Pair("data", "TEXT")
    )

}
