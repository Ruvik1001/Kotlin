package com.example.kotlin.model

class User(
    private var login: String = "",
    private var pass: String = "",
    private var name: String = "",
    private var last_name: String = "",
    private var  patronymic: String = "",
    private var post: String = "",
    private var telephone: String = ""
) {
    fun getLogin(): String { return login }
    fun getPassword(): String { return pass }
    fun getName(): String { return name }
    fun getLastName(): String { return last_name }
    fun getPatronymic(): String { return patronymic }
    fun getPost(): String { return post }
    fun getTelephone(): String { return telephone }

    fun setLogin(newLogin: String) { login =  newLogin }
    fun setPassword(newPass: String) { pass = newPass }
    fun setName(newName: String) { name = newName }
    fun setLastName(newLastName: String) { last_name = newLastName }
    fun setPatronymic(newPatronymic: String) { patronymic = newPatronymic }
    fun setPost(newPost: String) { post = newPost }
    fun setTelephone(newTelephone: String) { telephone = newTelephone }
}