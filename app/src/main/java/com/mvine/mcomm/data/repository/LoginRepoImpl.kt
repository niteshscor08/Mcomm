package com.mvine.mcomm.data.repository

import com.mvine.mcomm.data.repository.dataSource.LoginRemoteRepo
import com.mvine.mcomm.domain.repository.LoginRepository
import javax.inject.Inject

/**
 * An implementation of the [LoginRepository] class
 *
 * @param loginRemoteRepo The [LoginRemoteRepo] instance to handle remote Login calls
 */
class LoginRepoImpl @Inject constructor(
    private val loginRemoteRepo: LoginRemoteRepo
): LoginRepository {
    override suspend fun login(username: String, password: String) =
        loginRemoteRepo.login(username, password)

}