package com.example.kotlin.windows.support

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

public interface API {
    @GET("Ruvik1001/temp/main/food")
    fun getFood(): Call<ResponseBody>

    @GET("Ruvik1001/temp/main/drinks")
    fun getDrinks(): Call<ResponseBody>
}