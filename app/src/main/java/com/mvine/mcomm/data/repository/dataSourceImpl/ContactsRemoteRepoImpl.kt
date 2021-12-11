package com.mvine.mcomm.data.repository.dataSourceImpl

import com.mvine.mcomm.data.api.ContactsApiService
import com.mvine.mcomm.data.model.response.ContactsResponse
import com.mvine.mcomm.data.repository.dataSource.ContactsRemoteRepo
import com.mvine.mcomm.data.utils.safeApiCall
import com.mvine.mcomm.domain.util.Resource
import javax.inject.Inject

/**
 * An Implementation of the [ContactsRemoteRepo]
 *
 * @param contactsApiService The [ContactsApiService] instance to perform remote data calls
 */
class ContactsRemoteRepoImpl @Inject constructor(
    private val contactsApiService: ContactsApiService
) : ContactsRemoteRepo {
    override suspend fun getContacts(cookie: String): Resource<ContactsResponse> {
        return safeApiCall {
            contactsApiService.getRecentContacts(cookie)
        }
    }
}