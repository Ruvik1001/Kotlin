package com.example.kotlin.windows.hello

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin.windows.data.Product
import com.example.kotlin.windows.database.DBHelper
import com.example.kotlin.windows.special.drinksTableName
import com.example.kotlin.windows.special.productFiled
import com.example.kotlin.windows.special.retrofit
import com.example.kotlin.windows.support.API
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit

class HelloViewModel(
    val context: Context,
    load: (call: Call<ResponseBody>, callback: (String?)->Unit)->Unit,
    parser: (str: String)->List<Product>,
    saveProductList: (
        productList: List<Product>,
        tableName: String,
        tableFiled: List<Pair<String, String>>,
        reset: Boolean)->Unit
) : ViewModel() {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val service = retrofit.create(API::class.java)

            async {
               load(service.getDrinks()) {
                   if (it != null) {
                       saveProductList(
                           parser(it),
                           drinksTableName,
                           productFiled,
                           true
                       )
                   }
               }
            }
            async {
                load(service.getFood()) {
                    if (it != null) {
                        saveProductList(
                            parser(it),
                            drinksTableName,
                            productFiled,
                            true
                        )
                    }
                }
            }
        }
    }

}