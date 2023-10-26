package com.example.kotlin.database.remote

import com.example.kotlin.GlobalArgs
import com.example.kotlin.algorithms.Algorithms
import com.example.kotlin.user.User
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class DBFirebase {
    private var database = Firebase.database

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
                val ref = database.getReference(GlobalArgs().UserTableName)
                ref.child(sha256Login).setValue(
                    User(
                        login,
                        kombiPass,
                        encryptName,
                        encryptLastName,
                        encryptPatronymic,
                        encryptPost,
                        encryptTelephone)
                )
            }
        }
        callback(true)
    }



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

    fun auth(login: String, pass: String, callback: (User?) -> Unit) {
        val usersRef = database.getReference(GlobalArgs().UserTableName)

        val sha256Login = Algorithms().sha256(login)
        val kombiPass = Algorithms().kombi(pass)

        usersRef.orderByChild(sha256Login).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null && user.getPassword() == kombiPass) {
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


    fun connected(): Boolean {
        val connectedRef = Firebase.database.getReference(".info/connected")
        var success = false
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                success = snapshot.getValue(Boolean::class.java) ?: false
                return
            }

            override fun onCancelled(error: DatabaseError) {
                success = false
                return
            }
        })
        return success
    }
}
