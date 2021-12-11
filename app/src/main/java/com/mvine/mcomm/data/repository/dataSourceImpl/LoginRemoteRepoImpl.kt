package com.mvine.mcomm.data.repository.dataSourceImpl

import com.mvine.mcomm.data.api.LoginApiService
import com.mvine.mcomm.data.repository.dataSource.LoginRemoteRepo
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

/**
 * An Implementation of the [LoginRemoteRepo]
 *
 * @param loginApiService The [LoginApiService] instance to perform remote data calls
 */
class LoginRemoteRepoImpl @Inject constructor(
    private val loginApiService: LoginApiService
): LoginRemoteRepo {
    override suspend fun login(username: String, password: String): Response<ResponseBody> {
         return loginApiService.login(username, password, "", "Login")
    }
}