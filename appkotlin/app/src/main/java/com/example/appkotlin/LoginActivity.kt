package com.example.appkotlin

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import net.sqlcipher.database.SQLiteDatabase
import org.json.JSONArray
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Log.i(TAG, "START")

        val database:SQLiteDatabase = getDb()
        initDb(database)

    }

    ////////// Constantes //////////

    private var TAG:String = "LOGIN_MESSAGE"
    private var ERROR_FETCH:String = "error fetch"
    private val URL_CONFIG = "https://6007f1a4309f8b0017ee5022.mockapi.io/api/m1/config/1"
    private val URL_ACCOUNTS = "https://6007f1a4309f8b0017ee5022.mockapi.io/api/m1/accounts/"

    ////////// Database //////////

    private fun getDb(): SQLiteDatabase {
        SQLiteDatabase.loadLibs(this)
        val databaseFile = getDatabasePath("demo.db")
        return SQLiteDatabase.openOrCreateDatabase(databaseFile, "test123", null)
    }

    private fun initDb(database: SQLiteDatabase){
        database.execSQL("create table users(id, name, lastname, pin)")
    }

    private fun insertConfigToDb(database: SQLiteDatabase, config:JSONObject){
        database.execSQL("insert into users(id, name, lastname) values(?, ?, ?)",
                arrayOf<Any>(config.get("id").toString().toInt(), config.get("name").toString(),config.get("lastname").toString())
        )
    }

    private fun loadConfigFromDb(db: SQLiteDatabase):JSONObject{
        val config:JSONObject = JSONObject()
        val cursor: Cursor = db.rawQuery("select * from users", null)
        return config
    }



    ////////// OnClick functions //////////

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
                    val config: JSONObject = JSONObject()
                    config.put("id", user.get("id"))
                    config.put("name", user.get("name"))
                    config.put("lastname", user.get("lastname"))
                    val database: SQLiteDatabase = getDb()
                    insertConfigToDb(database, config)
                    // changer activity
                }
            }
        }
    }

    ////////// API request functions //////////

    // check user credentials, return user:JSONObject if ok, else return null
    private fun checkCredentialsOnline(name:String, lastname:String, cb:(user:JSONObject?, err:VolleyError?)->Unit){
        val queue = Volley.newRequestQueue(this)

        var ok:Boolean = false

        val stringRequest = StringRequest(Request.Method.GET, URL_CONFIG,
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

        val stringRequest = StringRequest(Request.Method.GET, URL_ACCOUNTS,
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