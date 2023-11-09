package com.example.kotlin.activity.fragment.about

import android.text.Html
import android.text.Spanned
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin.special.debug.DBG
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

class AboutViewModel() : ViewModel() {
    private val textLiveData = MutableLiveData<String>()
    val textData: LiveData<String> = textLiveData

    init {
        load()
    }

    interface GitHubService {
        @GET("/raw/asdsa-3876")
        fun _load(): Call<ResponseBody>
    }

    fun load() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://text-host.ru")
            .build()

        val service = retrofit.create(GitHubService::class.java)
        val call = service._load()

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    var body = response.body()?.string()
                    if (body != null) {
                        body = body.replace("\\n","<br>")
                        textLiveData.postValue(body)
                    }
                } else {
                    DBG().createLogE(response.code().toString())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                DBG().createLogE(t.toString())
            }
        })
    }
}
