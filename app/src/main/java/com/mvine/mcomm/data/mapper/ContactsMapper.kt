package com.mvine.mcomm.data.mapper

import com.mvine.mcomm.data.model.response.ContactsResponse
import com.mvine.mcomm.domain.model.ContactsData
import com.mvine.mcomm.domain.util.Resource

class ContactsMapper {

    fun entityToModel(contactsResponse: ContactsResponse?) : Resource<ArrayList<ContactsData>> {
        val contacts = arrayListOf<ContactsData>()
        contactsResponse?.callables?.forEach { contact ->
            contacts.add(
                ContactsData(
                    Company = contact.Company,
                    Department = contact.Department,
                    STX = contact.STX,
                    admin = contact.admin,
                    companyid = contact.companyid,
                    email = contact.email,
                    id = contact.id,
                    image_src = contact.image_src,
                    surname = contact.surname,
                    usename = contact.usename,
                    username = contact.username
                )
            )
        } ?: kotlin.run {
            return Resource.Error(message = "Error Fetching Calls Data")
        }
        return Resource.Success(data = contacts)
    }
}