package com.example.kotlin.special.debug

import android.util.Log

val dbg = DBG()
val TAG = "MyTag"

class DBG {
    fun createLog(type: String, tag: String, data: String) {
        when(type) {
            "v"     -> Log.v(tag, data)
            "i"     -> Log.i(tag, data)
            "d"     -> Log.d(tag, data)
            "w"     -> Log.w(tag, data)
            "e"     -> Log.e(tag, data)
            "wtf"   -> Log.wtf(tag, data)
        }
    }

    fun createLogV(data: String, tag: String = TAG) {
        createLog("v", tag, data)
    }

    fun createLogI(data: String, tag: String = TAG) {
        createLog("i", tag, data)
    }

    fun createLogD(data: String, tag: String = TAG) {
        createLog("d", tag, data)
    }

    fun createLogW(data: String, tag: String = TAG) {
        createLog("w", tag, data)
    }

    fun createLogE(data: String, tag: String = TAG) {
        createLog("e", tag, data)
    }

    fun createLogWTF(data: String, tag: String = TAG) {
        createLog("wtf", tag, data)
    }

}