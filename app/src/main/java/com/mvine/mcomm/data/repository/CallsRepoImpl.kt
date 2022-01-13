package com.mvine.mcomm.data.repository

import com.mvine.mcomm.data.mapper.CallsMapper
import com.mvine.mcomm.data.repository.dataSource.CallsRemoteRepo
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.repository.CallsRepository
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.util.EMPTY_STRING
import com.mvine.mcomm.util.LOGIN_TOKEN
import com.mvine.mcomm.util.NO_LOGIN_KEY_ERROR
import com.mvine.mcomm.util.PreferenceHandler
import javax.inject.Inject

class CallsRepoImpl @Inject constructor(
    private val callsRemoteRepo: CallsRemoteRepo,
    private val callsMapper: CallsMapper,
    private val preferenceHandler: PreferenceHandler
) : CallsRepository {

    override suspend fun getRecentCalls(): Resource<ArrayList<CallData>> {
        preferenceHandler.getValue(LOGIN_TOKEN)?.let { cookie ->
            val recentCalls =  callsRemoteRepo.getRecentCalls(cookie)
            return callsMapper.entityToModel(recentCalls.data)
        }
       return Resource.Error(NO_LOGIN_KEY_ERROR)
    }

}