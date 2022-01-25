package com.mvine.mcomm.data.repository

import com.mvine.mcomm.data.api.LogoutApiService
import com.mvine.mcomm.domain.repository.LogoutRepository
import com.mvine.mcomm.domain.util.Resource
import javax.inject.Inject

class LogoutRepositoryImpl @Inject constructor(
    private val logoutApiService: LogoutApiService
) : LogoutRepository {

    override suspend fun logout(cookie: String): Resource<Boolean?> {
        val response = logoutApiService.logout(cookie)
        return if (response.isSuccessful || response.raw().isRedirect) {
            Resource.Success(data = true)
        } else Resource.Error(message = "Error in Logging out")
    }
}
