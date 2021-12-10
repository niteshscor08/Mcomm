package com.mvine.mcomm.domain.usecase

import com.mvine.mcomm.domain.repository.ContactsRepository
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val contactsRepository: ContactsRepository
){
}