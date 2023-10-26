package com.example.kotlin.algorithms

import java.security.MessageDigest

class Algorithms {
    fun encrypt(text: String, _deep: Int = 3): String {
        var coded = ""
        val key = 0x8A9B

        for (i in 0 until text.length) {
            coded += ((text[i].code * (2 * (i.xor(0xA3)))).xor(key)).toChar()
        }

        return coded
    }

    fun decrypt(text: String): String {
        var coded = ""
        val key = 0x8A9B

        for (i in 0 until text.length) {
            coded += (text[i].code.xor(key) / (2 * (i.xor(0xA3)))).toChar()
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