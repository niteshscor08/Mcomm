package com.mvine.mcomm.domain.usecase

import com.mvine.mcomm.domain.repository.ContactsRepository
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val contactsRepository: ContactsRepository
){
    suspend fun getContacts(cookie: String) = contactsRepository.getContacts(cookie)
}