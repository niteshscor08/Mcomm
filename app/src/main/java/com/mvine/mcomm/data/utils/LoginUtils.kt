package com.mvine.mcomm.data.utils

import com.mvine.mcomm.data.model.LoginRequest

/**
 * Utils class to provide helper functions for Login feature
 */
object LoginUtils {
    fun prepareLoginRequestBody(username: String, password: String) =
        LoginRequest(credential_0 = username, credential_1 = password)
}