package com.example.appkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import okhttp3.*
import java.io.IOException
import java.net.URL


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
    private val client = OkHttpClient()

    private fun run(url: String) {

        //var url2= URL("https://6007f1a4309f8b0017ee5022.mockapi.io/api/m1/accounts").readText()
        //Log.d("url", url2)

        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e:IOException){}
            override fun onResponse(call:Call, response:Response) = println(response.body()?.string())
        })
    }
        


    fun refresh(button:View){
       run("https://6007f1a4309f8b0017ee5022.mockapi.io/api/m1/accounts")
    }

}


