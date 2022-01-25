package com.mvine.mcomm.domain.usecase

import com.mvine.mcomm.domain.repository.LoginRepository
import javax.inject.Inject

/**
 * A Use case to Handle Login Functionalities
 *
 * @param loginRepository The [LoginRepository] instance to handle login requests
 */

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    /**
     * Login the user with the username and password
     *
     * @param username The username of the user
     * @param password The password of the user
     */
    suspend fun login(username: String, password: String) = loginRepository.login(username, password)

    suspend fun getUserInfo(cookie: String) = loginRepository.getUserInfo(cookie)
}
