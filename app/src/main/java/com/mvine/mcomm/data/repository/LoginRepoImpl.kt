package com.mvine.mcomm.data.repository

import com.mvine.mcomm.data.api.LoginApiService
import com.mvine.mcomm.data.model.response.PersonInfo
import com.mvine.mcomm.data.utils.safeApiCall
import com.mvine.mcomm.domain.repository.LoginRepository
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.util.EMPTY_STRING
import javax.inject.Inject

/**
 * An implementation of the [LoginRepository] class
 *
 * @param loginRemoteRepo The [LoginRemoteRepo] instance to handle remote Login calls
 */
class LoginRepoImpl @Inject constructor(
    private val loginApiService: LoginApiService
) : LoginRepository {
    override suspend fun login(username: String, password: String): Resource<String?> {
        val response = try {
            loginApiService.login(username, password, EMPTY_STRING, LOGIN)
        } catch (exception: Exception) {
            return Resource.Error(message = "Error in Logging in")
        }
        return if (response.isSuccessful || response.raw().isRedirect) {
            val cookies = response.raw().headers("Set-Cookie")
            Resource.Success(data = validCookie(cookies))
        } else Resource.Error(message = "Error in Logging in")
    }

    override suspend fun getUserInfo(cookie: String): Resource<PersonInfo> {
        return safeApiCall { loginApiService.getUserView(cookie) }
    }

    private fun validCookie(cookies: List<String>): String? {
        return cookies.firstOrNull { !it.contains("HTTP_MVine_Session") && !it.contains("user:anon") }
    }

    companion object {
        const val LOGIN = "Login"
    }
}
