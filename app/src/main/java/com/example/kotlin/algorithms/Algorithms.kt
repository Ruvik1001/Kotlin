package com.example.kotlin.algorithms

import com.example.kotlin.GlobalArgs
import java.security.MessageDigest

class Algorithms {
    fun encrypt(text: String, _deep: Int = 3): String {
        var coded = ""

        for (i in 0 until text.length) {
            coded += ((text[i].code).xor(GlobalArgs().key1)).toChar()
        }

        return coded
    }

    fun decrypt(text: String): String {
        var coded = ""

        for (i in 0 until text.length) {
            coded += ((text[i].code).xor(GlobalArgs().key1)).toChar()
        }

        return coded
    }

    fun sha256(input: String): String {
        val bytes = input.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.joinToString("") { "%02x".format(it) }
    }

    fun kombi(input: String): String {
        return sha256(encrypt(input))
    }
}