package com.example.kotlin.activity.fragment.employe

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EmployeeViewModelFactory(private val context: Context, private val owner: LifecycleOwner) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EmployeeViewModel(
            context = context,
            owner = owner) as T
    }
}