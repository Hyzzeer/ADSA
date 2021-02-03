package com.example.appkotlin

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import net.sqlcipher.DatabaseUtils
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
        database.execSQL("create table if not exists accountsAndAmounts(id,account_name, amount, iban, currency)")
    }
    private fun clearAccountTable(database: SQLiteDatabase){

        database.execSQL("delete from accountsAndAmounts")
    }
    private fun insertAccountToDb(database: SQLiteDatabase, config:JSONObject){
        database.execSQL("insert into accountsAndAmounts(id,account_name, amount, iban, currency) values(?, ?, ? ,? ,?)",
                arrayOf<Any>(config.get("id").toString(),config.get("account_name").toString(), config.get("amount").toString(),config.get("iban").toString(),config.get("currency").toString())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val database:SQLiteDatabase = getDb()
        initDb(database)

    }
    private val client = OkHttpClient()

    private fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback{

            override fun onFailure(call: Call, e:IOException){}
            override fun onResponse(call:Call, response:Response) {

                var a=response.body()?.string()
                a=a.toString()
                val account:JSONArray = JSONArray(a)
                val db:SQLiteDatabase = getDb()
                clearAccountTable(db)

                for (i in 0 until account.length()) {
                    val item = account.getJSONObject(i)
                    insertAccountToDb(db,item)
                }
                //var cursor = db.rawQuery("select * from accountsAndAmounts", null)
                //println(DatabaseUtils.dumpCursorToString(cursor))
                //cursor.close()
            }
        })
    }
        

    fun refresh(button:View){
       run("https://6007f1a4309f8b0017ee5022.mockapi.io/api/m1/accounts")
    }

}


