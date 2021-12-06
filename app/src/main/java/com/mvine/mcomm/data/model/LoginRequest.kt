package com.mvine.mcomm.data.model

/**
 * Model class for Login Request
 *
 * @param credential_0: The Username
 * @param credential_1: The Password
 */
data class LoginRequest(
    val destination: String = "",
    val credential_0: String,
    val credential_1: String,
    val login: String = "Login"
)