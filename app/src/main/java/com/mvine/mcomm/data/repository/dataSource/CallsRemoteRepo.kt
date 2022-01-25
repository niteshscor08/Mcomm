package com.mvine.mcomm.data.repository.dataSource

import com.mvine.mcomm.data.model.response.CallablesResponse
import com.mvine.mcomm.data.model.response.CallsResponse
import com.mvine.mcomm.domain.util.Resource

/**
 * Base Domain Interface to handle Calls functionalities
 */
interface CallsRemoteRepo {
    suspend fun getRecentCalls(cookie: String): Resource<CallsResponse>

    suspend fun getAllCalls(cookie: String): Resource<CallablesResponse>
}
