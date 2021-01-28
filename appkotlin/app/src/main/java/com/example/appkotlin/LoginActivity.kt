package com.example.appkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.*
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun findUsernameById(username:String){
        val queue = Volley.newRequestQueue(this)
        val url_config = "https://6007f1a4309f8b0017ee5022.mockapi.io/api/m1/congif/"
        val url_acconts = "https://6007f1a4309f8b0017ee5022.mockapi.io/api/m1/accounts/"

        val stringRequest = StringRequest(Request.Method.GET, url_acconts,
                Response.Listener<String> { response ->
                    var resStr = response.toString()
                    val accounts: JSONArray = JSONArray(resStr)
                    var i = 0
                    while(i<accounts.length()){
                        var account: JSONObject = accounts.getJSONObject(i)
                        if (account.get("account_name") == usernameInput.text.toString()){

                        }
                        i+=1
                    }
                },
                Response.ErrorListener { Log.i("MYMESSAGE", "error fetch") })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }



}