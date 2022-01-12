package com.mvine.mcomm.domain.repository

import com.mvine.mcomm.domain.util.Resource

/**
 * Base Domain Interface to handle login functionalities
 */
interface LogoutRepository {
    suspend fun logout(cookie: String):  Resource<Boolean?>
}