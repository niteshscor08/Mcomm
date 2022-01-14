package com.mvine.mcomm.domain.repository

import com.mvine.mcomm.domain.util.Resource

interface ChangePasswordRepository {
    suspend fun changePassword(newPassword: String, reEnteredPassword: String): Resource<String>
}