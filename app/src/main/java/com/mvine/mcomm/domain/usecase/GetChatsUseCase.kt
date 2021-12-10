package com.mvine.mcomm.domain.usecase

import com.mvine.mcomm.domain.repository.ChatsRepository
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(
    private val chatsRepository: ChatsRepository
) {
}