package com.mvine.mcomm.data.repository.dataSource

/**
 * An Interface class to handle all remote requests for Login
 */
interface LoginRemoteRepo {

    /**
     * Logs the user with the username and password
     *
     * @param username The username of the user
     * @param password The password of the user
     */
    suspend fun login(username: String, password: String)
}