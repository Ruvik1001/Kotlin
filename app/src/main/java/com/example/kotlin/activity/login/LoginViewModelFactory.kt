package com.example.kotlin.activity.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin.activity.hostfragmentwindow.MainWindowActivity
import com.example.kotlin.data.FirebaseLiveData
import com.example.kotlin.special.database.local.DBSupport
import com.example.kotlin.special.database.remote.DBFirebase
import com.example.kotlin.special.dialog.AlertDialog
import com.example.kotlin.special.global.GlobalArgs
import com.example.kotlin.special.model.User

class LoginViewModelFactory(
    private val activity: Activity,
    private val localDB: DBSupport,
    private val remoteDB: DBFirebase,
    private val global: GlobalArgs,
    private val fbLive: FirebaseLiveData) : ViewModelProvider.Factory {

    fun auth(login: String, password: String) {
        if (global.mode == 1 && login == "test" && password == "test") {
            addUserToLocal(User(
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test"
            ))
            activity.startActivity(Intent(activity, MainWindowActivity::class.java))
            activity.finish()
            return
        }

        if (!activity.isFinishing && !activity.isDestroyed && !fbLive.connectedData.value!!) {
            AlertDialog().createCustomDialog(
                activity as Context,
                "Ошибка подключения!",
                "Проверьте подключение к интернету"
            )
            return
        }

        remoteDB.auth(login, password) {
            if (!activity.isFinishing && !activity.isDestroyed) {
                if (it == null) {
                    AlertDialog().createCustomDialog(
                        (activity as Context),
                        "Ошибка входа!",
                        "Пожалуйста, проверьте правильность введённых данных."
                    )
                    return@auth
                }

                addUserToLocal(it)
                activity.startActivity(Intent(activity, MainWindowActivity::class.java))
                activity.finish()
            }
        }
    }

    fun addUserToLocal(user: User) {
        val tableName = global.UserTableName
        val filed = global.UserFiled
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

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(
            localDataBase = localDB,
            remoteDataBase = remoteDB,
            firebaseLiveData = fbLive,
            globalArgs = global,
            auth = ::auth,
            addUserToLocal = ::addUserToLocal
        ) as T
    }
}