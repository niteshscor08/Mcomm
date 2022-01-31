package com.mvine.mcomm.data.repository

import com.mvine.mcomm.data.api.ChatsApiService
import com.mvine.mcomm.domain.repository.ChatsRepository
import javax.inject.Inject

class ChatsRepoImpl @Inject constructor(
    private val chatsApiService: ChatsApiService
) : ChatsRepository {

    override suspend fun getChats() {
    }
}
