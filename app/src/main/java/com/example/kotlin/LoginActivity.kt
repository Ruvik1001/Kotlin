package com.example.kotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin.database.local.DBSupport
import com.example.kotlin.database.remote.DBFirebase
import com.example.kotlin.databinding.ActivityLoginBinding
import com.example.kotlin.dialog.AlertDialog
import com.example.kotlin.model.User


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var remouteDB: DBFirebase = DBFirebase()
    private var connected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        remouteDB.connected() {
            connected = it
            dbg.createLogI("Firebase connected: " + connected.toString())
        }

        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Viktor1001")))
        }

        binding.btnLogin.setOnClickListener {
            if (!isFinishing && !isDestroyed) {
                if (!connected) {
                    AlertDialog().createCustomDialog(
                        this,
                        "Ошибка подключения!",
                        "Проверьте подключение к интернету"
                    )

                } else
                    auth()
            }

        }
    }

    fun auth() {
        remouteDB.auth(binding.loginLog.text.toString(), binding.loginPassword.text.toString()) {
            if (!isFinishing && !isDestroyed) {
                if (it == null) {
                    AlertDialog().createCustomDialog(
                        this,
                        "Ошибка входа!",
                        "Пожалуйста, проверьте правильность введённых данных."
                    )
                    return@auth
                }

                addUserToLocal(it)
                val intent = Intent(this, MainWindowActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }




    fun addUserToLocal(user: User) {
        val localDB = DBSupport(this)
        val tableName = GlobalArgs().UserTableName
        val filed = GlobalArgs().UserFiled
        localDB.selectTable(tableName, filed)
        localDB.clearSelectedTable()
        localDB.addDataToCurrentTable(listOf<Pair<String,String>>(
            Pair(filed[0].first, user.getLogin()),
            Pair(filed[1].first, user.getPassword()),
            Pair(filed[2].first, user.getName()),
            Pair(filed[3].first, user.getLastName()),
            Pair(filed[4].first, user.getPatronymic()),
            Pair(filed[5].first, user.getPost()),
            Pair(filed[6].first, user.getTelephone()),
        ))
        val t = localDB.getAllDataFromCurrentTable()
        for (row in t)
            for (elem in row)
                Log.d("MyTag", elem)
    }
}