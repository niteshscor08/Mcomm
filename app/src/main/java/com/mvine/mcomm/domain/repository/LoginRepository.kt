package com.mvine.mcomm.domain.repository

/**
 * Base Domain Interface to handle login functionalities
 */
interface LoginRepository {

    /**
     * Login the user with the username and password
     *
     * @param username The username of the user
     * @param password The password of the user
     */
    suspend fun login(username: String, password: String)
}