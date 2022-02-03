package com.mvine.mcomm.data.repository

import com.mvine.mcomm.data.api.CallsApiService
import com.mvine.mcomm.data.mapper.AllCallsMapper
import com.mvine.mcomm.data.mapper.CallsMapper
import com.mvine.mcomm.data.utils.safeApiCall
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.repository.CallsRepository
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.util.LOGIN_TOKEN
import com.mvine.mcomm.util.NO_LOGIN_KEY_ERROR
import com.mvine.mcomm.util.PreferenceHandler
import javax.inject.Inject

class CallsRepoImpl @Inject constructor(
    private val callsApiService: CallsApiService,
    private val callsMapper: CallsMapper,
    private val preferenceHandler: PreferenceHandler,
    private val allCallsMapper: AllCallsMapper
) : CallsRepository {

    override suspend fun getRecentCalls(): Resource<ArrayList<CallData>> {
        preferenceHandler.getValue(LOGIN_TOKEN)?.let { cookie ->
            val recentCalls = safeApiCall {
                callsApiService.getRecentCalls(cookie)
            }
            return callsMapper.entityToModel(recentCalls.data)
        }
        return Resource.Error(NO_LOGIN_KEY_ERROR)
    }

    override suspend fun getAllCalls(): Resource<ArrayList<CallData>> {
        preferenceHandler.getValue(LOGIN_TOKEN)?.let { cookie ->
            val allCalls = safeApiCall {
                callsApiService.getAllCalls(cookie)
            }
            return allCallsMapper.entityToModel(allCalls.data)
        }
        return Resource.Error(NO_LOGIN_KEY_ERROR)
    }
}
