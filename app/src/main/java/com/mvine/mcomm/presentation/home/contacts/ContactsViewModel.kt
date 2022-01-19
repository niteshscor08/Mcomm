package com.mvine.mcomm.presentation.home.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mvine.mcomm.domain.model.ContactsData
import com.mvine.mcomm.domain.usecase.GetContactsUseCase
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.presentation.common.base.BaseViewModel
import com.mvine.mcomm.util.PreferenceHandler
import com.mvine.mcomm.util.sortDataAlphabetically
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val dispatcher: CoroutineDispatcher
    ): BaseViewModel() {

    private val _contactsLiveData: MutableLiveData<Resource<ArrayList<ContactsData>>> =
        MutableLiveData()
    val contacts: LiveData<Resource<ArrayList<ContactsData>>> = _contactsLiveData

    private val _searchCallsLiveData: MutableLiveData<ArrayList<ContactsData>> =
        MutableLiveData()
    val searchCalls: LiveData<ArrayList<ContactsData>> = _searchCallsLiveData

    init {
        getContacts()
    }

    private fun getContacts() {
            viewModelScope.launch(dispatcher) {
                _contactsLiveData.apply {
                    postValue(Resource.Loading())
                    postValue(getContactsUseCase.getContacts())
                }

        }
    }

    fun filterData(query: String) {
        _contactsLiveData.value?.data?.filter {
            it.username?.contains(query, ignoreCase = true) == true
        }?.let {
            _searchCallsLiveData.postValue(it as ArrayList<ContactsData>)
        }
    }

    fun sortAlphabetically(arrayList: ArrayList<ContactsData>): ArrayList< ContactsData >{
        return sortDataAlphabetically(arrayList) { o1: ContactsData, o2: ContactsData ->
            o1.username.toString().lowercase().compareTo(o2.username.toString().lowercase())
        }
    }
}