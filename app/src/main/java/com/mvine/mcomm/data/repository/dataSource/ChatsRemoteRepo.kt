package com.mvine.mcomm.data.repository.dataSource

/**
 * Base Domain Interface to handle Chats functionalities
 */
interface ChatsRemoteRepo {

    suspend fun getChats()
}