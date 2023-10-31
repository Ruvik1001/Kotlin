package com.example.kotlin.koin

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import com.example.kotlin.activity.fragment.employe.EmployeeViewModelFactory
import com.example.kotlin.activity.fragment.job.JobViewModelFactory
import com.example.kotlin.activity.login.LoginViewModelFactory
import com.example.kotlin.data.FirebaseLiveData
import com.example.kotlin.special.algorithms.Algorithms
import com.example.kotlin.special.database.local.DBSupport
import com.example.kotlin.special.database.remote.DBFirebase
import com.example.kotlin.special.global.GlobalArgs
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val moduleApp = module {
    factory { (activity: Activity) ->
        LoginViewModelFactory(
            activity,
            get(),
            get(),
            get(),
            get()
        )
    }

    factory { (activity: Activity) ->
        val owner: LifecycleOwner = activity as LifecycleOwner // Приведение Activity к LifecycleOwner
        JobViewModelFactory(androidContext(), owner)
    }

    factory { (activity: Activity) ->
        val owner: LifecycleOwner = activity as LifecycleOwner // Приведение Activity к LifecycleOwner
        EmployeeViewModelFactory(androidContext(), owner)
    }

    single {
        DBSupport(androidContext())
    }

    single {
        DBFirebase(get())
    }

    single {
        GlobalArgs()
    }

    single {
        FirebaseLiveData(get())
    }

    single {
        Algorithms()
    }

}