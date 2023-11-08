package com.example.kotlin.windows.hello

import com.example.kotlin.windows.database.DBHelper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlin.R
import com.example.kotlin.windows.fragments.host.HostWindowActivity
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
        startActivity(Intent(this, HostWindowActivity::class.java))
        finish()
    }
}