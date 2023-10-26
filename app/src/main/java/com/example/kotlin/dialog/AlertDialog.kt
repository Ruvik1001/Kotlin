package com.example.kotlin.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

class AlertDialog {
    fun createCustomDialog(
        context: Context,
        title: String,
        message: String,
        buttonCount: Int,
        buttonLabels: Array<String>,
        buttonActions: Array<() -> Unit>
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)

        for (i in 0 until buttonCount) {
            builder.setPositiveButton(buttonLabels[i]) { dialog, which ->
                if (i < buttonActions.size) {
                    buttonActions[i].invoke()
                }
            }
        }

        val dialog = builder.create()
        dialog.show()
    }
}