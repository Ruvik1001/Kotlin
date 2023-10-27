package com.example.kotlin.model

import com.google.firebase.database.PropertyName

class Task(
    @PropertyName("login") private var login: String = "",
    @PropertyName("label") private var label: String = "",
    @PropertyName("text") private var text: String = "",
    @PropertyName("date_from") private var date_from: String = "",
    @PropertyName("date_to") private var date_to: String = "",
    @PropertyName("status") private var status: String = ""
) {


    fun getLogin(): String { return login }
    fun getLabel(): String { return label }
    fun getText(): String { return text }
    fun getDateFrom(): String { return date_from }
    fun getDateTo(): String { return date_to }
    fun getStatus(): String { return status }

    fun setLogin(newLogin: String) { login =  newLogin }
    fun setLabel(newLabel: String) { label = newLabel }
    fun setText(newText: String) { text = newText }
    fun setDateFrom(newDateFrom: String) { date_from = newDateFrom }
    fun setDateTo(newDateTo: String) { date_to = newDateTo }
    fun setStatus(newStatus: String) { status = newStatus }
}