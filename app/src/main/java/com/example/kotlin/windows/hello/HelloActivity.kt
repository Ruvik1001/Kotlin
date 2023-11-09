package com.example.kotlin.windows.hello

import com.example.kotlin.windows.database.DBHelper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin.R
import com.example.kotlin.windows.fragments.host.HostWindowActivity
import com.example.kotlin.windows.special.drinksTableName
import com.example.kotlin.windows.special.foodTableName
import com.example.kotlin.windows.special.productFiled
import com.example.kotlin.windows.support.API
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import kotlin.concurrent.thread

class HelloActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)

        val factory : HelloViewModelFactory by inject()
        val helloViewModel = ViewModelProvider(this, factory).get(HelloViewModel::class.java)

//        startActivity(Intent(this, HostWindowActivity::class.java))
//        finish()
    }
}