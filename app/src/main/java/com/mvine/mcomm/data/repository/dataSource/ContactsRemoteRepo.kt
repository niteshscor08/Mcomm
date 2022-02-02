package com.mvine.mcomm.data.repository.dataSource

import com.mvine.mcomm.data.model.response.ContactsResponse
import com.mvine.mcomm.domain.util.Resource

/**
 * Base Domain Interface to handle Contacts functionalities
 */
interface ContactsRemoteRepo {

    suspend fun getContacts(cookie: String): Resource<ContactsResponse>
}
