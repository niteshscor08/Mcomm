package com.mvine.mcomm.data.repository.dataSourceImpl

import com.mvine.mcomm.data.api.CallsApiService
import com.mvine.mcomm.data.repository.dataSource.CallsRemoteRepo
import javax.inject.Inject

/**
 * An Implementation of the [CallsRemoteRepo]
 *
 * @param callsApiService The [CallsApiService] instance to perform remote data calls
 */
class CallsRemoteRepoImpl @Inject constructor(
    private val callsApiService: CallsApiService
): CallsRemoteRepo {
    override suspend fun getCalls() {

    }
}