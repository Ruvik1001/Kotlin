package com.example.kotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin.algorithms.Algorithms
import com.example.kotlin.database.local.DBSupport
import com.example.kotlin.database.remote.DBFirebase
import com.example.kotlin.databinding.ActivityLoginBinding
import com.example.kotlin.dialog.AlertDialog
import com.example.kotlin.user.User


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var remouteDB: DBFirebase = DBFirebase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Viktor1001")))
        }

        binding.btnLogin.setOnClickListener {
//            if (!remouteDB.connected()) {
//                AlertDialog().createCustomDialog(
//                    this,
//                    "Ошибка подключения!",
//                    "Проверьте подключение к интернету",
//                    1,
//                    arrayOf("Ок"),
//                    arrayOf({})
//                )
//                return@setOnClickListener
//            }
            remouteDB.auth(binding.loginLog.text.toString(), binding.loginPassword.text.toString()) {
                if (it != null) {
                    //addUserToLocal(it)
                    AlertDialog().createCustomDialog(
                        this,
                        "!",
                        (it.getLogin() + " " + it.getPassword() + " " + it.getName() + " " + it.getLastName() + " " + it.getPatronymic() + " " + it.getPost() + " " + it.getTelephone()),
                        1,
                        arrayOf("Ок"),
                        arrayOf({})
                    )
                    val intent = Intent(this, MainWindowActivity::class.java)
                    startActivity(intent)
                    //finish()
                    return@auth
                }
                else {
                    AlertDialog().createCustomDialog(
                        this,
                        "Ошибка вохода!",
                        "Пожалуйста, проверьте правильность введённых данных.",
                        1,
                        arrayOf("Ок"),
                        arrayOf({})
                    )
                    return@auth
                }
            }

        }
        remouteDB.addUser(
            "ruvik1001@gmail.com",
            "Gfhjkm007q",
            "Viktor",
            "Rudnev",
            "Vladimirovich",
            "Engenear",
            "+1(111)111-11-11"
            ) {}

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
    }
}