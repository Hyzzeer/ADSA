package com.example.appkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
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

    private var TAG:String = "LOGIN_MESSAGE"
    private var ERROR_FETCH:String = "error fetch"

    fun onClickConnexion(button:View){
        checkCredentialsOnline(nameInput.text.toString(), lastnameInput.text.toString()) { user:JSONObject?, err:VolleyError? ->
            if (err!=null) loginMessage.text = ERROR_FETCH
            else{
                if ((user==null)){
                    loginMessage.text = "Wrong credentials"
                }
                else{
                    loginMessage.text = "Good"
                    // save user info dans bdd
                    // changer activity
                }
            }
        }
    }

    private val url_config = "https://6007f1a4309f8b0017ee5022.mockapi.io/api/m1/config/1"
    val url_accounts = "https://6007f1a4309f8b0017ee5022.mockapi.io/api/m1/accounts/"

    // check user credentials, return user:JSONObject if ok, else return null
    private fun checkCredentialsOnline(name:String, lastname:String, cb:(user:JSONObject?, err:VolleyError?)->Unit){
        val queue = Volley.newRequestQueue(this)

        var ok:Boolean = false

        val stringRequest = StringRequest(Request.Method.GET, url_config,
                Response.Listener<String>{ response ->
                    val resStr:String = response.toString()
                    val user:JSONObject = JSONObject(resStr)
                    ok = (user.get("name").toString()==name) and (user.get("lastname").toString()==lastname)
                    if (ok) cb(user, null)
                    else cb(null, null)
                },
                Response.ErrorListener { error ->
                    cb(null, error)
                })

        queue.add(stringRequest)
        //queue.start()
    }

    private fun findAccountsOnline(cb:(accounts:JSONArray?, err:VolleyError?)->Unit){
        val queue = Volley.newRequestQueue(this)

        lateinit var accounts: JSONArray

        val stringRequest = StringRequest(Request.Method.GET, url_accounts,
                Response.Listener<String> { response ->
                    val resStr:String = response.toString()
                    accounts = JSONArray(resStr)
                    cb(accounts, null)
                },
                Response.ErrorListener { error ->
                    cb(null, error)
                })

        queue.add(stringRequest)
    }
}