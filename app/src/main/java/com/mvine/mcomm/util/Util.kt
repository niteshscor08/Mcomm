package com.mvine.mcomm.util

fun extractEpochTime(token : String): Long {
    var epochTime = EMPTY_STRING
    token.forEach {
        epochTime += when {
            it.isDigit() -> {
                it
            }
            else -> {
                return epochTime.toLong()
            }
        }
    }
    return epochTime.toLong()
}