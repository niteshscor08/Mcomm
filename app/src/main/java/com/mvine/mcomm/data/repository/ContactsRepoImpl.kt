package com.mvine.mcomm.data.repository

import com.mvine.mcomm.data.mapper.ContactsMapper
import com.mvine.mcomm.data.repository.dataSource.ContactsRemoteRepo
import com.mvine.mcomm.domain.model.ContactsData
import com.mvine.mcomm.domain.repository.ContactsRepository
import com.mvine.mcomm.domain.util.Resource
import javax.inject.Inject

class ContactsRepoImpl @Inject constructor(
    private val contactsRemoteRepo: ContactsRemoteRepo,
    private val contactsMapper: ContactsMapper
): ContactsRepository {

    override suspend fun getContacts(cookie: String): Resource<ArrayList<ContactsData>> {
        val contacts = contactsRemoteRepo.getContacts(cookie)
        return contactsMapper.entityToModel(contacts.data)
    }

}