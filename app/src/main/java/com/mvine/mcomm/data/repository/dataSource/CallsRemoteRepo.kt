package com.mvine.mcomm.data.repository.dataSource


/**
 * Base Domain Interface to handle Calls functionalities
 */
interface CallsRemoteRepo {
    suspend fun getCalls()
}