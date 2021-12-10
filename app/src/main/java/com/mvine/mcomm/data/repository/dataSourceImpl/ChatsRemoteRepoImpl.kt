package com.mvine.mcomm.data.repository.dataSourceImpl

import com.mvine.mcomm.data.api.ChatsApiService
import com.mvine.mcomm.data.api.LoginApiService
import com.mvine.mcomm.data.repository.dataSource.ChatsRemoteRepo
import com.mvine.mcomm.data.utils.LoginUtils
import com.mvine.mcomm.domain.util.Resource
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

/**
 * An Implementation of the [ChatsRemoteRepo]
 *
 * @param chatsApiService The [ChatsApiService] instance to perform remote data calls
 */
class ChatsRemoteRepoImpl @Inject constructor(
    private val chatsApiService: ChatsApiService
): ChatsRemoteRepo {
    override suspend fun getChats() {

    }
}