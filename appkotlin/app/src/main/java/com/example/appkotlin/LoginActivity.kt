package com.example.appkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.android.volley.*
import com.android.volley.toolbox.*
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONObject

import net.sqlcipher.database.SQLiteDatabase


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        SQLiteDatabase.loadLibs(this)
        val databaseFile = getDatabasePath("demo.db")
        if(databaseFile.exists()) databaseFile.delete()
        databaseFile.mkdirs()
        databaseFile.delete()
        val database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "test123", null)
        database.execSQL("create table t1(a, b)")
        database.execSQL("insert into t1(a, b) values(?, ?)",
            arrayOf<Any>("one for the money", "two for the show")
        )

        Log.i(TAG, "START")

    }

    var TAG = "LOGIN_MESSAGE"

    fun connexionListener(button:Button){
        findUsernameById(usernameInput.text.toString())
    }

    fun findUsernameById(username:String){
        val queue = Volley.newRequestQueue(this)
        val url_config = "https://6007f1a4309f8b0017ee5022.mockapi.io/api/m1/congif/"
        val url_accounts = "https://6007f1a4309f8b0017ee5022.mockapi.io/api/m1/accounts/"

        val stringRequest = StringRequest(Request.Method.GET, url_accounts,
                Response.Listener<String> { response ->
                    var resStr = response.toString()
                    val accounts: JSONArray = JSONArray(resStr)
                    var i = 0
                    var found:Boolean = false
                    while((i<accounts.length()) and (!found)){
                        var account: JSONObject = accounts.getJSONObject(i)
                        if (account.get("account_name").toString() == usernameInput.text.toString()){
                            // put user in bdd
                            // navigate to main activity
                            found=true
                        }
                        i+=1
                    }
                    if (found) infoText.text="FOUND"
                    else infoText.text="NOT FOUND"
                },
                Response.ErrorListener {
                    infoText.text="ERROR FETCH"
                    Log.i(TAG, "error fetch")
                })
        queue.add(stringRequest)
    }
}