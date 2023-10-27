package com.example.kotlin.database.remote

import com.example.kotlin.GlobalArgs
import com.example.kotlin.algorithms.Algorithms
import com.example.kotlin.model.Task
import com.example.kotlin.model.TasksList
import com.example.kotlin.model.User
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.util.LinkedList

class DBFirebase {
    private var database = Firebase.database

    ////////////////////ADD////////////////////

    fun <T> addAny(tableName: String, obj: T, child: LinkedList<String>? = null) {
        var ref : DatabaseReference = database.getReference(tableName)
        if (child != null)
            for (elem in child)
                ref = ref.child(elem)
        ref.setValue(obj)
    }

    fun addUser(
        login: String,
        pass: String,
        name: String,
        last_name: String,
        patronymic: String,
        post: String,
        telephone: String,
        callback: (Boolean) -> Unit) {
        val sha256Login = Algorithms().sha256(login)
        val kombiPass = Algorithms().kombi(pass)
        val encryptName = Algorithms().encrypt(name)
        val encryptLastName = Algorithms().encrypt(last_name)
        val encryptPatronymic = Algorithms().encrypt(patronymic)
        val encryptPost = Algorithms().encrypt(post)
        val encryptTelephone = Algorithms().encrypt(telephone)

        findUser(sha256Login) { userExists ->
            if (userExists) {
                callback(false)
            } else {
                addAny<User>(
                    GlobalArgs().UserTableName,
                    User(
                        login,
                        kombiPass,
                        encryptName,
                        encryptLastName,
                        encryptPatronymic,
                        encryptPost,
                        encryptTelephone),
                        LinkedList(listOf(sha256Login))
                    )
            }
        }
        callback(true)
    }

    fun addTask(
        login: String,
        label: String,
        text: String,
        date_from: String,
        date_to: String,
        status: String,
        callback: (Boolean) -> Unit
    ) {
        val sha256Login = Algorithms().sha256(login)
        val encryptLabel = Algorithms().encrypt(label)
        val encryptText = Algorithms().encrypt(text)
        val encryptDateFrom = Algorithms().encrypt(date_from)
        val encryptDateTo = Algorithms().encrypt(date_to)
        val encryptStatus = Algorithms().encrypt(status)

        val task = Task(
            login,
            encryptLabel,
            encryptText,
            encryptDateFrom,
            encryptDateTo,
            encryptStatus
        )

        val nonEncrTask = Task(
            login,
            label,
            text,
            date_from,
            date_to,
            status
        )

        findTasks(login) { tasksList ->
            val existingTasks = tasksList.get()
            for (elem in existingTasks) {
                if (
                    elem.getLogin() == login &&
                    elem.getLabel() == label &&
                    elem.getText() == text &&
                    elem.getDateFrom() == date_from &&
                    elem.getDateTo() == date_to &&
                    elem.getStatus() == status
                ) {
                    callback(false)
                    return@findTasks
                }
            }
            existingTasks.add(task)
            val updatedTasksList = TasksList(existingTasks)
            for (elem in updatedTasksList.get()) {
                val child_ref = database.reference.push()
                val child = LinkedList(
                    listOf(
                        sha256Login,
                        Algorithms().sha256(child_ref.key ?: "ErrGenKey")
                    )
                )
                addAny(
                    GlobalArgs().TaskTableName,
                    elem,
                    child
                )
            }
            callback(true)

        }
    }



    ////////////////////FIND////////////////////

    fun findUser(login: String, callback: (Boolean) -> Unit) {
        val usersRef = database.getReference(GlobalArgs().UserTableName)

        val sha256Login = Algorithms().sha256(login)

        usersRef.orderByChild(sha256Login).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null && user.getLogin() == login) {
                            callback(true)
                            return
                        }
                    }
                }
                callback(false)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(false)
            }
        })
    }

    fun findTasks(login: String, callback: (TasksList) -> Unit) {
        val ref = database.getReference(GlobalArgs().TaskTableName)
        val sha256Login = Algorithms().sha256(login)

        ref.orderByChild(sha256Login).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val tasks = mutableListOf<Task>()
                    for (snapshot in dataSnapshot.children) {
                        for (data in snapshot.children) {
                            val task = data.getValue(Task::class.java)
                            if (task != null && task.getLogin() == login) {
                                val decryptedTask = Task(
                                    task.getLogin(),
                                    Algorithms().decrypt(task.getLabel()),
                                    Algorithms().decrypt(task.getText()),
                                    Algorithms().decrypt(task.getDateFrom()),
                                    Algorithms().decrypt(task.getDateTo()),
                                    Algorithms().decrypt(task.getStatus())
                                )
                                tasks.add(decryptedTask)
                            }
                        }
                    }
                    val tasksList = TasksList(LinkedList(tasks))
                    callback(tasksList)
                } else {
                    callback(TasksList())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(TasksList())
            }
        })
    }


    ////////////////////GET////////////////////

    fun <T : Any> getAllFrom(tableName: String, type: Class<T>, callback: (List<T>?) -> Unit) {
        val ref = database.getReference(tableName)
        val itemList = mutableListOf<T>()

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        val item = snapshot.getValue(type)
                        if (item != null) {
                            itemList.add(item)
                        }
                    }
                    callback(itemList)
                } else {
                    callback(null)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }

    fun getAllUsers(callback: (List<User>?) -> Unit) {
        getAllFrom(GlobalArgs().UserTableName, User::class.java) { users ->
            if (users != null) {
                val processedUsers = users.map { user ->
                    User(
                        user.getLogin(),
                        "null",
                        Algorithms().decrypt(user.getName()),
                        Algorithms().decrypt(user.getLastName()),
                        Algorithms().decrypt(user.getPatronymic()),
                        Algorithms().decrypt(user.getPost()),
                        Algorithms().decrypt(user.getTelephone())
                    )
                }
                callback(processedUsers)
            } else {
                callback(null)
            }
        }
    }


    fun getAllTasks(login: String, callback: (TasksList) -> Unit) {
        val ref = database.getReference(GlobalArgs().TaskTableName)
        val sha256Login = Algorithms().sha256(login)

        ref.orderByChild(sha256Login).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val tasks = mutableListOf<Task>()
                    for (snapshot in dataSnapshot.children) {
                        for (data in snapshot.children) {
                            val task = data.getValue(Task::class.java)
                            if (task != null) {
                                val decryptedTask = Task(
                                    task.getLogin(),
                                    Algorithms().decrypt(task.getLabel()),
                                    Algorithms().decrypt(task.getText()),
                                    Algorithms().decrypt(task.getDateFrom()),
                                    Algorithms().decrypt(task.getDateTo()),
                                    Algorithms().decrypt(task.getStatus())
                                )
                                tasks.add(decryptedTask)
                            }
                        }
                    }
                    val tasksList = TasksList(LinkedList(tasks))
                    callback(tasksList)
                } else {
                    callback(TasksList())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(TasksList())
            }
        })
    }


    //fun getAllUsers(callback: (List<User>?) -> Unit) {
    //        val usersRef = database.getReference(GlobalArgs().UserTableName)
    //        val userList = mutableListOf<User>()
    //
    //        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
    //            override fun onDataChange(dataSnapshot: DataSnapshot) {
    //                if (dataSnapshot.exists()) {
    //                    for (userSnapshot in dataSnapshot.children) {
    //                        userSnapshot.getValue(User::class.java)?.let { userList.add(User(
    //                            it.getLogin(),
    //                            "null",
    //                            Algorithms().decrypt(it.getName()),
    //                            Algorithms().decrypt(it.getLastName()),
    //                            Algorithms().decrypt(it.getPatronymic()),
    //                            Algorithms().decrypt(it.getPost()),
    //                            Algorithms().decrypt(it.getTelephone())
    //                        )) }
    //                    }
    //                    callback(userList)
    //                }
    //                else
    //                    callback(null)
    //            }
    //
    //            override fun onCancelled(databaseError: DatabaseError) {
    //                callback(null)
    //            }
    //        })
    //    }

    ////////////////////SPECIAL////////////////////

    fun auth(login: String, pass: String, callback: (User?) -> Unit) {
        val usersRef = database.getReference(GlobalArgs().UserTableName)

        val sha256Login = Algorithms().sha256(login)
        val kombiPass = Algorithms().kombi(pass)

        usersRef.orderByChild(sha256Login).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null && user.getLogin() == login && user.getPassword() == kombiPass) {
                            callback(
                                User(
                                    user.getLogin(),
                                    "null",
                                    Algorithms().decrypt(user.getName()),
                                    Algorithms().decrypt(user.getLastName()),
                                    Algorithms().decrypt(user.getPatronymic()),
                                    Algorithms().decrypt(user.getPost()),
                                    Algorithms().decrypt(user.getTelephone())
                                ))
                            return
                        }
                    }
                }
                callback(null)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }

    fun connected(callback: (Boolean) -> Unit) {
        val connectedRef = Firebase.database.getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val success = snapshot.getValue(Boolean::class.java) ?: false
                callback(success)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }
        })
    }

}
