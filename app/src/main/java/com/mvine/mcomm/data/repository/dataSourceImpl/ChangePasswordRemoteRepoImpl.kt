package com.mvine.mcomm.data.repository.dataSourceImpl

import com.mvine.mcomm.data.api.ChangePasswordApiService
import com.mvine.mcomm.data.model.response.ChangePasswordResponse
import com.mvine.mcomm.data.repository.dataSource.ChangePasswordRemoteRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ChangePasswordRemoteRepoImpl @Inject constructor(
    private val changePasswordApiService: ChangePasswordApiService
) : ChangePasswordRemoteRepo {

    override suspend fun changePassword(
        cookie: String,
        newPassword: String,
        reEnteredPassword: String
    ): Response<ChangePasswordResponse> {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(newPassword, reEnteredPassword)
            .build()
        return changePasswordApiService.changePassword(cookie, requestBody)
    }
}
