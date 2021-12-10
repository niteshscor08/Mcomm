package com.mvine.mcomm.domain.repository

/**
 * Base Domain Interface to handle Contacts functionalities
 */
interface ContactsRepository {

    suspend fun getContacts()
}