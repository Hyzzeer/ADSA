package com.example.appkotlin

import android.content.Intent
import android.database.Cursor
import android.database.SQLException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activty_pin.*
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteException
import org.json.JSONObject
import java.lang.Exception
import kotlin.math.log


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)

        val db: SQLiteDatabase = getDb()
        val pinUser:String? = getPinFromDb(db)
        if (pinUser==null){
            // navigate to login
            Log.i(TAG,"navigate to login")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        else {
            // navigate to put your secret pin
            Log.i(TAG,"put your secret pin")
            setContentView(R.layout.activty_pin);
        }
    }

    ////////// OnClick functions //////////

    private var TAG:String = "LOGIN_MESSAGE"
    private var pin:String = ""

    ////////// On ck=lc //////////

    fun onClickPinButton(v: View){
        val button = v as Button // cast view en button
        val radioArray = arrayOf(radioButton1, radioButton2, radioButton3, radioButton4)
        if (!button.text.isNullOrEmpty()){
            if ((pin.length>=0) and (pin.length<4)){
                pin += button.text.toString()
                radioArray[pin.length-1].isChecked = true
            }
        }
        if (button.id == buttonDelete.id){
            if ((pin.length>0) and (pin.length<=4)) {
                radioArray[pin.length - 1].isChecked = false
                pin = pin.slice(0 until pin.length - 1)
            }
        }
        if (button.id == buttonEmpty.id){
            if (pin.length==4) {
                val db = getDb()
                val pinUser:String? = getPinFromDb(db)
                if (pin == pinUser){
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                else pinMessage.text = "pin didn't match"
            }
            else{
                pinMessage.text = "put 4 pins"
            }
        }
    }

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