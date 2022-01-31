package com.mvine.mcomm.data.repository

import com.mvine.mcomm.data.api.ContactsApiService
import com.mvine.mcomm.data.mapper.ContactsMapper
import com.mvine.mcomm.data.utils.safeApiCall
import com.mvine.mcomm.domain.model.ContactsData
import com.mvine.mcomm.domain.repository.ContactsRepository
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.util.LOGIN_TOKEN
import com.mvine.mcomm.util.NO_LOGIN_KEY_ERROR
import com.mvine.mcomm.util.PreferenceHandler
import javax.inject.Inject

class ContactsRepoImpl @Inject constructor(
    private val contactsApiService: ContactsApiService,
    private val contactsMapper: ContactsMapper,
    private val preferenceHandler: PreferenceHandler
) : ContactsRepository {

    override suspend fun getContacts(): Resource<ArrayList<ContactsData>> {
        preferenceHandler.getValue(LOGIN_TOKEN)?.let { cookie ->
            val contacts = safeApiCall {
                contactsApiService.getRecentContacts(cookie)
            }
            return contactsMapper.entityToModel(contacts.data)
        }
        return Resource.Error(NO_LOGIN_KEY_ERROR)
    }
}
