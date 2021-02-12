package com.example.appkotlin

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.appkotlin.Helper.aaaaaaaaaaa.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activty_pin.*
import net.sqlcipher.database.SQLiteDatabase
import org.json.JSONArray
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


    }

    ////////// Constantes //////////

    private var ERROR_FETCH:String = "error fetch"
    private val URL_CONFIG:String = Lorem+ipsum+dolor+sit+amet+adipiscing+favor+abajo+antes+ahora+como+donde+efecto+suma+nada+izquierda+terminar

    private var pin:String = ""
    private val config: JSONObject = JSONObject()

    ////////// Database //////////

    private fun getDb(pin:String): SQLiteDatabase {
        SQLiteDatabase.loadLibs(this)
        val databaseFile = getDatabasePath("demo.db")
        return SQLiteDatabase.openOrCreateDatabase(databaseFile, pin, null)
    }

    private fun initDb(database: SQLiteDatabase){
        database.execSQL("create table if not exists users(id, name, lastname)")
    }

    private fun logout(){
        SQLiteDatabase.loadLibs(this)
        val databaseFile = getDatabasePath("demo.db")
        databaseFile.delete()
    }

    private fun insertConfigToDb(database: SQLiteDatabase, config:JSONObject){
        database.execSQL("insert into users(id, name, lastname) values(?, ?, ?)",
                arrayOf<Any>(config.get("id").toString(), config.get("name").toString(),config.get("lastname").toString())
        )
    }

    ////////// OnClick functions //////////

    fun onClickConnexion(v:View){
        checkCredentialsOnline(nameInput.text.toString(), lastnameInput.text.toString()) { user:JSONObject?, err:VolleyError? ->
            if (err!=null) loginMessage.text = ERROR_FETCH
            else{
                if ((user==null)){
                    loginMessage.text = "Wrong credentials"
                }
                else{
                    loginMessage.text = "Good"
                    // save user info dans bdd
                    config.put("id", user.get("id"))
                    config.put("name", user.get("name"))
                    config.put("lastname", user.get("lastname"))

                    setContentView(R.layout.activty_pin);
                }
            }
        }
    }

    fun onClickValidButton(v: View){
        val password1 = inputPass.text.toString()

        if ((8 <= password1.length) and (password1.length <= 20)) {
            val database: SQLiteDatabase = getDb(password1)
            initDb(database)
            insertConfigToDb(database, config)
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
        }
        else{
            pinMessage.text = "Le mot de passe doit comporter entre 8 et 20 caractères"
        }
    }



    ////////// API request functions //////////

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
    }
}