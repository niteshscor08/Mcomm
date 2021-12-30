package com.mvine.mcomm.data.repository

import com.mvine.mcomm.data.mapper.CallsMapper
import com.mvine.mcomm.data.model.response.PersonInfo
import com.mvine.mcomm.data.repository.dataSource.CallsRemoteRepo
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.repository.CallsRepository
import com.mvine.mcomm.domain.util.Resource
import javax.inject.Inject

class CallsRepoImpl @Inject constructor(
    private val callsRemoteRepo: CallsRemoteRepo,
    private val callsMapper: CallsMapper
) : CallsRepository {

    override suspend fun getRecentCalls(cookie: String): Resource<ArrayList<CallData>> {
        val recentCalls =  callsRemoteRepo.getRecentCalls(cookie)
        return callsMapper.entityToModel(recentCalls.data)
    }

    override suspend fun getUserInfo(cookie: String): Resource<PersonInfo> {
        return callsRemoteRepo.getUserInfo(cookie)
    }

}