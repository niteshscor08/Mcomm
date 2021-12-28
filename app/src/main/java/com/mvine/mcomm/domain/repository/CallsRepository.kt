package com.mvine.mcomm.domain.repository

import com.mvine.mcomm.data.model.response.PersonInfo
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.util.Resource

/**
 * Base Domain Interface to handle Calls functionalities
 */
interface CallsRepository {

    suspend fun getRecentCalls(cookie: String): Resource<ArrayList<CallData>>

    suspend fun getUserInfo(cookie: String):  Resource<PersonInfo>
}