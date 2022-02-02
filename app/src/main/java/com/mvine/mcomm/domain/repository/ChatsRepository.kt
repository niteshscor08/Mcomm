package com.mvine.mcomm.domain.repository

/**
 * Base Domain Interface to handle Chats functionalities
 */
interface ChatsRepository {

    suspend fun getChats()
}
