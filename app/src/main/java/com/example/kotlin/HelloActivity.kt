package com.example.kotlin

import DBHelper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.concurrent.thread

class HelloActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)
        thread{
            load()
        }
    }

    private fun load() {
        val db = DBHelper(this, null)
        db.onCreate(db.writableDatabase)

        if (db.getPositions()?.count?.toInt()!! < 1) {
            db.loadFromResource(R.array.kitchen_menu, false)
            db.loadFromResource(R.array.bar_menu, false)
        }

        db.close()

        Thread.sleep(2300)
        startActivity(Intent(this, MainWindowActivity::class.java))
        finish()
    }
}