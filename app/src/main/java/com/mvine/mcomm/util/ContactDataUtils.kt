package com.mvine.mcomm.util

import com.mvine.mcomm.domain.model.ContactsData
import com.mvine.mcomm.presentation.common.ListInteraction
import com.mvine.mcomm.presentation.common.RowType
import com.mvine.mcomm.presentation.common.viewtypes.ContactRowType

fun prepareRowTypesFromContactsData(
    contactsData: ArrayList<ContactsData>,
    interaction: ListInteraction<ContactsData>,
    setDefaultData: Boolean = false
): ArrayList<RowType> {
    val rowTypes = arrayListOf<RowType>()
    contactsData.forEach { contactsDataItem ->
        if(setDefaultData) contactsDataItem.isExpanded = false
        rowTypes.add(ContactRowType(contactsDataItem, interaction))
    }
    return rowTypes
}