package com.example.kotlin.windows.koin

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import com.example.kotlin.windows.database.DBHelper
import com.example.kotlin.windows.hello.HelloViewModelFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val moduleApp = module {
    single {
        DBHelper(androidContext())
    }

    factory {
        HelloViewModelFactory(androidContext(), get())
    }


}