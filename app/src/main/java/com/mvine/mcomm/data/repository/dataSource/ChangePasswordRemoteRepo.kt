package com.mvine.mcomm.data.repository.dataSource

import com.mvine.mcomm.data.model.response.ChangePasswordResponse
import retrofit2.Response

interface ChangePasswordRemoteRepo {
    suspend fun changePassword(cookie: String, newPassword: String, reEnteredPassword: String): Response<ChangePasswordResponse>
}
