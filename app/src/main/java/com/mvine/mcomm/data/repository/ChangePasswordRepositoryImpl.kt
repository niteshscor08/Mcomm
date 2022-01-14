package com.mvine.mcomm.data.repository

import com.mvine.mcomm.data.repository.dataSource.ChangePasswordRemoteRepo
import com.mvine.mcomm.domain.repository.ChangePasswordRepository
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.util.LOGIN_TOKEN
import com.mvine.mcomm.util.PreferenceHandler
import javax.inject.Inject

class ChangePasswordRepositoryImpl @Inject constructor(
    private val changePasswordRemoteRepo: ChangePasswordRemoteRepo,
    private val preferenceHandler : PreferenceHandler
) : ChangePasswordRepository{
    override suspend fun changePassword(newPassword: String, reEnteredPassword: String): Resource<String> {
        preferenceHandler.getValue(LOGIN_TOKEN)?.let { cookie ->
            val response =  try {
                changePasswordRemoteRepo.changePassword(cookie, newPassword, reEnteredPassword)
            } catch (exception: Exception) {
                return Resource.Error(message = "Error in Password Update")
            }
            if(response.isSuccessful){
                val passwordUpdatedMessage : String?=  response.body()?.update_password
                return  passwordUpdatedMessage?.let {
                    Resource.Success(data = it)
                }?: Resource.Error(message = "Error in Password Update")
            }
        }
        return Resource.Error(message = "Error in Password Update")
    }
}