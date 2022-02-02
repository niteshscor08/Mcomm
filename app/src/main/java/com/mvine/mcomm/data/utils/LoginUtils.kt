package com.mvine.mcomm.data.utils

import okhttp3.FormBody

/**
 * Utils class to provide helper functions for Login feature
 */
object LoginUtils {
    fun prepareLoginRequestBody(username: String, password: String): FormBody {
        return FormBody.Builder()
            .add("destination", "")
            .add("credential_0", username)
            .add("credential_1", password)
            .add("Login", "Login")
            .build()
    }
}
