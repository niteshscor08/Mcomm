package com.mvine.mcomm.data.repository

import com.mvine.mcomm.data.repository.dataSource.LoginRemoteRepo
import com.mvine.mcomm.domain.repository.LoginRepository
import com.mvine.mcomm.domain.util.Resource
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

/**
 * An implementation of the [LoginRepository] class
 *
 * @param loginRemoteRepo The [LoginRemoteRepo] instance to handle remote Login calls
 */
class LoginRepoImpl @Inject constructor(
    private val loginRemoteRepo: LoginRemoteRepo
): LoginRepository {
    override suspend fun login(username: String, password: String): Resource<String?> {
        val response =  loginRemoteRepo.login(username, password)
        return if (response.isSuccessful) {
            Resource.Success(data = response.headers()["Set-Cookie"])
        } else Resource.Error(message = "Error in Logging in")
    }

}