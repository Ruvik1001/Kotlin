package com.example.kotlin.activity.login

import androidx.lifecycle.ViewModel
import com.example.kotlin.data.FirebaseLiveData
import com.example.kotlin.special.database.local.DBSupport
import com.example.kotlin.special.database.remote.DBFirebase
import com.example.kotlin.special.global.GlobalArgs
import com.example.kotlin.special.model.User

class LoginViewModel(
    private val localDataBase: DBSupport,
    private val remoteDataBase: DBFirebase,
    private val firebaseLiveData: FirebaseLiveData,
    private val globalArgs: GlobalArgs,
    val auth: (login: String, password: String) -> Unit,
    val addUserToLocal: (user: User) -> Unit
) : ViewModel() {

    val remoteDB = remoteDataBase
    val firebaseLD = firebaseLiveData
    val global = globalArgs

}