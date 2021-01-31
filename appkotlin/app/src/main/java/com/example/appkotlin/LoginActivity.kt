package com.example.appkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        var user:JSONObject? = checkCredentialsOnline(nameInput.text.toString(), lastnameInput.text.toString())
        if ((user!=null)){
            loginMessage.text = "Good"
            // save user info dans bdd
            // changer activity
        }
        else if (user==null){
            loginMessage.text = "Wrong credentials"
        }
    }

    val url_config = "https://6007f1a4309f8b0017ee5022.mockapi.io/api/m1/config/1"
    val url_accounts = "https://6007f1a4309f8b0017ee5022.mockapi.io/api/m1/accounts/"

    // check user credentials, return user:JSONObject if ok, else return null
    private fun checkCredentialsOnline(name:String, lastname:String):JSONObject?{
        val queue = Volley.newRequestQueue(this)

        var user:JSONObject? = null
        var ok:Boolean = false

        val stringRequest = StringRequest(Request.Method.GET, url_config,
                Response.Listener<String>{ response ->
                    val resStr:String = response.toString()
                    user = JSONObject(resStr)
                    ok = (user!!.get(name).toString()==name) and (user!!.get(name).toString()==name)
                },
                Response.ErrorListener { error ->
                    Log.i(TAG, error.toString())
                    loginMessage.text = ERROR_FETCH
                })
        queue.add(stringRequest)
        queue.start()
        return if (ok) user
        else null
    }

    private fun findAccountsOnline():JSONArray{
        val queue = Volley.newRequestQueue(this)

        lateinit var accounts: JSONArray

        val stringRequest = StringRequest(Request.Method.GET, url_accounts,
                Response.Listener<String> { response ->
                    val resStr:String = response.toString()
                    accounts = JSONArray(resStr)
                },
                Response.ErrorListener {
                    Log.i(TAG, "error fetch")
                })
        queue.add(stringRequest)
        return accounts
    }

    fun findAccountsByAccountNameOnline(account_name:String): JSONArray {
        val accounts:JSONArray = findAccountsOnline()
        lateinit var accountsFound: JSONArray
        var i = 0
        while((i<accounts.length())){
            var account: JSONObject = accounts.getJSONObject(i)
            if (account.get("account_name").toString() == account_name){
                accountsFound.put(account)
            }
            i+=1
        }
        return accountsFound
    }
}