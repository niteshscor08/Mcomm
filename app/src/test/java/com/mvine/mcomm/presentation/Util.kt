package com.mvine.mcomm.presentation

import android.content.Context
import java.io.BufferedReader
import java.io.InputStream

/**
 * This funtion is reading data from json file and returning a string
 *
 * @param filename
 * @param context
 * @return string of json file data
 */
fun readFile(filename: String, context: Context): String {
    val inputStream: InputStream = context.javaClass.classLoader!!.getResourceAsStream(filename)
    val reader = BufferedReader(inputStream.reader())
    val content = StringBuilder()
    reader.use { reader ->
        var line = reader.readLine()
        while (line != null) {
            content.append(line)
            line = reader.readLine()
        }
    }
    return content.toString()
}
