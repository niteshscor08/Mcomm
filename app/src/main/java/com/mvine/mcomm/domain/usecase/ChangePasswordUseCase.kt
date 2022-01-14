package com.mvine.mcomm.domain.usecase

import com.mvine.mcomm.domain.repository.ChangePasswordRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val changePasswordRepository: ChangePasswordRepository
) {
    suspend fun changePassword( newPassword: String, reEnteredPassword:String) =
        changePasswordRepository.changePassword(newPassword,reEnteredPassword)
}