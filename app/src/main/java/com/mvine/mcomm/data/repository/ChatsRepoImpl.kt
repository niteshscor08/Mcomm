package com.mvine.mcomm.data.repository

import com.mvine.mcomm.data.repository.dataSource.ChatsRemoteRepo
import com.mvine.mcomm.domain.repository.ChatsRepository
import javax.inject.Inject

class ChatsRepoImpl @Inject constructor(
    private val chatsRemoteRepo: ChatsRemoteRepo
) : ChatsRepository {

    override suspend fun getChats() {
    }
}
