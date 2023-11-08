package com.example.kotlin.windows.special

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

fun createAlert(
    context: Context,
    title: String = "",
    message: String = "",
    buttonsList: List<Pair<String, ()->Unit>> = listOf()
) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)

    for (button in buttonsList)
        builder.setPositiveButton(button.first) { _, _ -> button.second.invoke() }

    val dialog = builder.create()
    dialog.show()
}

fun makeToast(
    context: Context,
    text: String,
    duration: Int = Toast.LENGTH_LONG
) {
    Toast.makeText(context, text, duration).show()
}