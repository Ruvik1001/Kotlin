package com.example.kotlin.windows.hello

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin.windows.data.Product
import com.example.kotlin.windows.database.DBHelper
import com.example.kotlin.windows.special.productFiled
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HelloViewModelFactory(val context: Context, val dataBase: DBHelper) : ViewModelProvider.Factory {
    val mutex = Mutex()


    fun load(call: Call<ResponseBody>, callback: (String?)->Unit) {
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    callback(response.body()?.string())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.message?.let { Log.e("MyT", it) }
                callback(null)
            }
        })
    }

    fun parser(str: String) : List<Product> {
        val mutableList = mutableListOf<Product>()

        var items =  str
            .replace("[", "")
            .replace("\"", "")
            .replace(",", "")
            .split("]]")
            .toList()

        items = items.subList(0, items.size - 1)

        for (productString in items) {
            val productItems = productString.split("]").toList()
            mutableList.add(
                Product(
                productItems[0],
                productItems[1],
                productItems[2],
                productItems[3]
            )
            )
        }

        return mutableList
    }

    fun saveProductList(
        productList: List<Product>,
        tableName: String,
        tableFiled: List<Pair<String, String>>,
        reset: Boolean = false
    ) {
        Log.e("MyT", "saveProductList")
        CoroutineScope(Dispatchers.IO).launch {
            mutex.lock()
            Log.e("MyT", "mutex lock")
            try {
                dataBase.selectTable(tableName, tableFiled)

                if (reset)
                    dataBase.clearSelectedTable()

                for (product in productList)
                    dataBase.addDataToCurrentTable(
                        getWritableProduct(product)
                    )
            } finally {
                Log.e("MyT", "mutex unlock")
                mutex.unlock()
            }
        }
    }

    fun getWritableProduct(product: Product): List<Pair<String,String>> {
        return listOf(
            Pair(productFiled[0].first, product.position),
            Pair(productFiled[1].first, product.cost),
            Pair(productFiled[2].first, product.count),
            Pair(productFiled[3].first, product.imageSrc)
        )
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HelloViewModel(
            context = context,
            load = ::load,
            parser = ::parser,
            saveProductList = ::saveProductList
        ) as T
    }
}