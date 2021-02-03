package com.example.appkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import okhttp3.*
import java.io.IOException
import java.net.URL
import net.sqlcipher.database.SQLiteDatabase
import org.json.JSONArray
import org.json.JSONObject


class HomeActivity : AppCompatActivity() {

    private var TAG:String = "LOGIN_MESSAGE"
    private val config: JSONObject = JSONObject()

    private fun getDb(): SQLiteDatabase {
        SQLiteDatabase.loadLibs(this)
        val databaseFile = getDatabasePath("demo.db")
        Log.i(TAG, databaseFile.toString())
        return SQLiteDatabase.openOrCreateDatabase(databaseFile, "test123", null)
    }

    private fun initDb(database: SQLiteDatabase){
        database.execSQL("create table if not exists accountsAndAmounts(account, amount)")
    }
    private fun insertAccountToDb(database: SQLiteDatabase, config:JSONObject){
        database.execSQL("insert into accountsAndAmounts(account, amount) values(?, ?)",
                arrayOf<Any>(config.get("account").toString(), config.get("amount").toString())
        )
    }

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

            override fun onResponse(call:Call, response:Response) {
                var a=response.body()?.string()
                a=a.toString()
                a=a.dropLast(1)
                a=a.drop(1)
                val account:JSONObject = JSONObject(a)

                println(account)

            }


        })

    }
        


    fun refresh(button:View){
       run("https://6007f1a4309f8b0017ee5022.mockapi.io/api/m1/accounts")
    }

}


