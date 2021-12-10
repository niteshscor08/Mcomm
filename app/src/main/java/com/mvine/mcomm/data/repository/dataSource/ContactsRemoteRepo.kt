package com.mvine.mcomm.data.repository.dataSource


/**
 * Base Domain Interface to handle Contacts functionalities
 */
interface ContactsRemoteRepo {

    suspend fun getContacts()
}