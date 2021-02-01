package com.example.appkotlin

import android.database.Cursor
import android.database.SQLException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteException
import org.json.JSONObject
import java.lang.Exception
import kotlin.math.log


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val db: SQLiteDatabase = getDb()
        val pin:String? = getPinFromDb(db)
        if (pin==null){
            // navigate to login
            Log.i(TAG,"navigate to login")
        }
        else if (pin==""){
            // navigate to put your secret pin
            Log.i(TAG,"put your secret pin")
        }
        else {
            // navigate to main activity
            Log.i(TAG,"navigate to main activity")
        }
    }

    ////////// Constantes //////////

    private var TAG:String = "LOGIN_MESSAGE"

    ////////// Database //////////

    private fun getDb(): SQLiteDatabase {
        SQLiteDatabase.loadLibs(this)
        val databaseFile = getDatabasePath("demo.db")
        Log.i(TAG, databaseFile.toString())
        return SQLiteDatabase.openOrCreateDatabase(databaseFile, "test123", null)
    }

    private fun initDb(database: SQLiteDatabase){
        database.execSQL("create table if not exists users(id, name, lastname, pin)")
    }

    private fun logout(database: SQLiteDatabase){
        database.execSQL("drop table if exists users");
        initDb(database)
    }


    private fun getPinFromDb(database: SQLiteDatabase):String?{
        val cursor: Cursor = database.rawQuery("select * from users limit 1", null)
        cursor.moveToFirst();
        return try {
            val data:String = cursor.getString(cursor.getColumnIndex("pin"));
            cursor.close()
            Log.i(TAG, data.toString())
            data
        }catch (err:Exception){
            cursor.close()
            null
        }
    }

    private fun checkPinWithDb(database: SQLiteDatabase, pin:Int):Boolean{
        return true
    }


}