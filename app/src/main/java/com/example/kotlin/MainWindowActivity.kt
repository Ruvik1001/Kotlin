package com.example.kotlin

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.kotlin.databinding.ActivityMainWindowBinding
import com.example.kotlin.Support

class MainWindowActivity : AppCompatActivity(), Support {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_window)
    }

    override fun OnTasksButtonClicked() {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.MainWindowFragmentContainerView, Tasks())
        transaction.addToBackStack(null);
        transaction.commit()
    }
}