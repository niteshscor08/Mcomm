package com.mvine.mcomm.domain.usecase

import com.mvine.mcomm.domain.repository.CallsRepository
import javax.inject.Inject

class GetCallsUseCase @Inject constructor(
    private val callsRepository: CallsRepository
) {
    suspend fun getRecentCalls(cookie: String) =
        callsRepository.getRecentCalls(cookie)

    suspend fun getAllCalls(cookie: String) =
        callsRepository.getAllCalls(cookie)

    suspend fun getUserInfo(cookie: String) = callsRepository.getUserInfo(cookie)

}