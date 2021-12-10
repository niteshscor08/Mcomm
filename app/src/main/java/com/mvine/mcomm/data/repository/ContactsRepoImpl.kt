package com.mvine.mcomm.data.repository

import com.mvine.mcomm.data.repository.dataSource.ContactsRemoteRepo
import com.mvine.mcomm.domain.repository.ContactsRepository
import javax.inject.Inject

class ContactsRepoImpl @Inject constructor(
    private val contactsRemoteRepo: ContactsRemoteRepo
): ContactsRepository {

    override suspend fun getContacts() {

    }

}