package com.mvine.mcomm.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * @desc this method is used to hide soft Keyboard
 */
fun EditText.hideKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * @desc A Method to show the keyboard for the Respective Edit Text
 */
fun EditText.showKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(this, 0)
}