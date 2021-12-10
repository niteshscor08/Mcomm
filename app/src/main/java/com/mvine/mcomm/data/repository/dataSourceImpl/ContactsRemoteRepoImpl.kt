package com.mvine.mcomm.data.repository.dataSourceImpl

import com.mvine.mcomm.data.api.ContactsApiService
import com.mvine.mcomm.data.repository.dataSource.ContactsRemoteRepo
import javax.inject.Inject

/**
 * An Implementation of the [ContactsRemoteRepo]
 *
 * @param contactsApiService The [ContactsApiService] instance to perform remote data calls
 */
class ContactsRemoteRepoImpl @Inject constructor(
    private val contactsApiService: ContactsApiService
): ContactsRemoteRepo {
    override suspend fun getContacts() {

    }
}