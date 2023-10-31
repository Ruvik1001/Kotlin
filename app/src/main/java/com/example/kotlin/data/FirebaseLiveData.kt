package com.example.kotlin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kotlin.special.database.remote.DBFirebase
import com.example.kotlin.special.debug.DBG
import com.example.kotlin.special.model.TasksList
import com.example.kotlin.special.model.User

class FirebaseLiveData(remoteDB: DBFirebase) {
    private val connectedLiveData = MutableLiveData<Boolean>()
    val connectedData: LiveData<Boolean> = connectedLiveData

    private val usersLiveData = MutableLiveData<List<User>>()
    val usersData: LiveData<List<User>> = usersLiveData

    private val tasksLiveData = MutableLiveData<TasksList>()
    val tasksData: LiveData<TasksList> = tasksLiveData

    init {
        remoteDB.connected { connection ->
            setConnection(connection)
            DBG().createLogI("DBFB connection $connection")
            if (connection) {
                remoteDB.getAllUsers {
                    if (it != null) {
                        setUsers(it)
                    }
                }
                remoteDB.getAllTasks("") { setTasks(it) }
            }
        }
    }

    private fun setConnection(value: Boolean) { DBG().createLogI("DBFB connection set called"); connectedLiveData.postValue(value) }
    private fun setUsers(users: List<User>) { DBG().createLogI("DBFB connection set users called"); usersLiveData.postValue(users) }
    private fun setTasks(tasks: TasksList) { DBG().createLogI("DBFB connection set tasks called"); tasksLiveData.postValue(tasks) }
}