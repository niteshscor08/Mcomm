package com.mvine.mcomm.data.repository

import com.mvine.mcomm.data.repository.dataSource.CallsRemoteRepo
import com.mvine.mcomm.domain.repository.CallsRepository
import javax.inject.Inject

class CallsRepoImpl @Inject constructor(
    private val callsRemoteRepo: CallsRemoteRepo
): CallsRepository {

    override suspend fun getCalls() {

    }

}