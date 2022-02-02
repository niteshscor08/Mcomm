package com.mvine.mcomm.janus.utils

import java.util.Random

class RandomString {
    val str = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val rnd = Random()
    fun randomString(length: Int): String {
        val sb = StringBuilder(length)
        for (i in 0 until length) {
            sb.append(str[rnd.nextInt(str.length)])
        }
        return sb.toString()
    }
}
