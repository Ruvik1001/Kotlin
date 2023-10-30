package com.example.kotlin.activity.hostfragmentwindow

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlin.R
import com.example.kotlin.special.support.SupportLinking
import com.example.kotlin.activity.fragment.job.Job

class MainWindowActivity : AppCompatActivity(), SupportLinking {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_window)
    }

    override fun OnTasksButtonClicked() {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.MainWindowFragmentContainerView, Job())
        transaction.addToBackStack(null);
        transaction.commit()
    }
}