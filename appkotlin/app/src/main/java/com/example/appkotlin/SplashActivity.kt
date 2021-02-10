package com.example.appkotlin

import android.annotation.SuppressLint
import android.content.Intent
//import android.database.Cursor
import android.database.SQLException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activty_pin.*
import net.sqlcipher.Cursor
import net.sqlcipher.DatabaseUtils
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteException
import org.json.JSONObject
import java.lang.Exception
import kotlin.math.log


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)

        //val db: SQLiteDatabase = getDb()
        // val pinUser:String? = getPinFromDb(db)
        // if (pinUser==null){
        // navigate to login
        // Log.i(TAG,"navigate to login")
        // val intent = Intent(this, LoginActivity::class.java)
        // startActivity(intent)
        // }
        // else {
        // navigate to put your secret pin
        // Log.i(TAG,"put your secret pin")
        // setContentView(R.layout.activty_pin);
        //}


        if (!isDbExist()) {
            // navigate to login
            Log.i(TAG, "navigate to login")

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            // navigate to put your secret pin
            Log.i(TAG, "put your secret pin")

            setContentView(R.layout.activty_pin);
        }


    }

    ////////// OnClick functions //////////

    private var TAG: String = "LOGIN_MESSAGE"
    private var pin: String = ""
    private var COUNT_MAX: Int = 3

    ////////// On ck=lc //////////

    fun onClickValidButton( v: View ) {
        val password1 = inputPass.text.toString()


        if ((8 <= password1.length) and (password1.length <= 20)) {
            try {
                getDb(password1)
                //val cursor: Cursor = db.rawQuery("select * from users", null)
                //Log.i(TAG, DatabaseUtils.dumpCursorToString(cursor).toString())
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("pin", password1)
                startActivity(intent)
            } catch (err: SQLiteException) {
                COUNT_MAX--
                pinMessage.text = "Les mots de passes ne correspondent pas , il vous reste $COUNT_MAX essais"
            }
            if (COUNT_MAX == 0) {
                logout()
                val intent = Intent(this, SplashActivity::class.java)
                startActivity(intent)
            }
        }
    }



    ////////// Database //////////

    private fun isDbExist(): Boolean {
        SQLiteDatabase.loadLibs(this)
        val databaseFile = getDatabasePath("demo.db")
        return databaseFile.exists()
    }

    private fun getDb(pin: String): SQLiteDatabase {
        SQLiteDatabase.loadLibs(this)
        val databaseFile = getDatabasePath("demo.db")
        return SQLiteDatabase.openOrCreateDatabase(databaseFile, pin, null)
    }


    private fun logout() {
        SQLiteDatabase.loadLibs(this)
        val databaseFile = getDatabasePath("demo.db")
        databaseFile.delete()
    }
}