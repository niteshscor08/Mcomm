package com.mvine.mcomm.data.repository.dataSourceImpl

import com.mvine.mcomm.data.api.CallsApiService
import com.mvine.mcomm.data.model.response.CallsResponse
import com.mvine.mcomm.data.model.response.PersonInfo
import com.mvine.mcomm.data.repository.dataSource.CallsRemoteRepo
import com.mvine.mcomm.data.utils.safeApiCall
import com.mvine.mcomm.domain.util.Resource
import retrofit2.Response
import javax.inject.Inject

/**
 * An Implementation of the [CallsRemoteRepo]
 *
 * @param callsApiService The [CallsApiService] instance to perform remote data calls
 */
class CallsRemoteRepoImpl @Inject constructor(
    private val callsApiService: CallsApiService
): CallsRemoteRepo {
    override suspend fun getRecentCalls(cookie: String): Resource<CallsResponse> {
        return safeApiCall {
            callsApiService.getRecentCalls(cookie)
        }
    }

    override suspend fun getUserInfo(cookie: String): Resource<PersonInfo> {
        return safeApiCall {
            callsApiService.getUserView(cookie)
        }
    }
}