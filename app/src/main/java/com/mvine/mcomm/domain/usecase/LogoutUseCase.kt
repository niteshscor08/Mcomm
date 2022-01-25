package com.mvine.mcomm.domain.usecase

import com.mvine.mcomm.domain.repository.LoginRepository
import com.mvine.mcomm.domain.repository.LogoutRepository
import javax.inject.Inject

/**
 * A Use case to Handle Login Functionalities
 *
 * @param loginRepository The [LoginRepository] instance to handle login requests
 */

class LogoutUseCase @Inject constructor(
    private val logoutRepository: LogoutRepository
) {

    suspend fun logout(cookie: String) = logoutRepository.logout(cookie)
}
