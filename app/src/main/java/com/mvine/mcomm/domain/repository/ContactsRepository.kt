package com.mvine.mcomm.domain.repository

import com.mvine.mcomm.domain.model.ContactsData
import com.mvine.mcomm.domain.util.Resource

/**
 * Base Domain Interface to handle Contacts functionalities
 */
interface ContactsRepository {

    suspend fun getContacts(cookie: String): Resource<ArrayList<ContactsData>>
}