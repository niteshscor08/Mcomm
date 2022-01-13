package com.mvine.mcomm.domain.repository

import com.mvine.mcomm.data.model.response.PersonInfo
import com.mvine.mcomm.domain.util.Resource

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
    suspend fun login(username: String, password: String): Resource<String?>

    suspend fun getUserInfo(cookie: String):  Resource<PersonInfo>

}