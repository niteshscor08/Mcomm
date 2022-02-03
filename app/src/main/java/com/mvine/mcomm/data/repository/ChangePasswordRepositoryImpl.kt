package com.mvine.mcomm.data.repository

import com.mvine.mcomm.data.api.ChangePasswordApiService
import com.mvine.mcomm.domain.repository.ChangePasswordRepository
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.util.LOGIN_TOKEN
import com.mvine.mcomm.util.PreferenceHandler
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ChangePasswordRepositoryImpl @Inject constructor(
    private val changePasswordApiService: ChangePasswordApiService,
    private val preferenceHandler: PreferenceHandler
) : ChangePasswordRepository {
    override suspend fun changePassword(newPassword: String, reEnteredPassword: String): Resource<String> {
        preferenceHandler.getValue(LOGIN_TOKEN)?.let { cookie ->
            val response = try {
                changePasswordApiService.changePassword(cookie, getRequestBody(newPassword, reEnteredPassword))
            } catch (exception: Exception) {
                return Resource.Error(message = "Error in Password Update")
            }
            if (response.isSuccessful) {
                val passwordUpdatedMessage: String? = response.body()?.update_password
                return passwordUpdatedMessage?.let {
                    Resource.Success(data = it)
                } ?: Resource.Error(message = "Error in Password Update")
            }
        }
        return Resource.Error(message = "Error in Password Update")
    }

    fun getRequestBody(
        newPassword: String,
        reEnteredPassword: String
    ): RequestBody {
        return MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(newPassword, reEnteredPassword)
            .build()
    }
}
