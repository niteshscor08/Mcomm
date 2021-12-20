package com.mvine.mcomm.domain.usecase

import com.mvine.mcomm.domain.repository.ChangePasswordRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val changePasswordRepository: ChangePasswordRepository
) {
    suspend fun changePassword(cookie: String, newPassword: String) =
        changePasswordRepository.changePassword(cookie,newPassword)
}