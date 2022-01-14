package com.mvine.mcomm.domain.repository

import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.util.Resource

/**
 * Base Domain Interface to handle Calls functionalities
 */
interface CallsRepository {
    suspend fun getAllCalls(): Resource<ArrayList<CallData>>

    suspend fun getRecentCalls(): Resource<ArrayList<CallData>>
}