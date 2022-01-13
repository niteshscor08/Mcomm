package com.mvine.mcomm.util

import android.content.Context
import android.widget.Toast

/**
 * Context Extension function for showing Toast
 *
 * @param message Customized Message string to be used to display in the toast
 * @param length The duration for which the toast needs to be shown
 */
fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}