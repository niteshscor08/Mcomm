package com.mvine.mcomm.presentation.home.contacts

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvine.mcomm.domain.model.ContactsData
import com.mvine.mcomm.domain.usecase.GetContactsUseCase
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.util.LOGIN_TOKEN
import com.mvine.mcomm.util.MCOMM_SHARED_PREFERENCES
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val dispatcher: CoroutineDispatcher,
    @ApplicationContext context: Context
): ViewModel() {

    private val _contactsLiveData: MutableLiveData<Resource<ArrayList<ContactsData>>> =
        MutableLiveData()
    val contacts: LiveData<Resource<ArrayList<ContactsData>>> = _contactsLiveData

    private val sharedPreferences =
        context.getSharedPreferences(MCOMM_SHARED_PREFERENCES, Context.MODE_PRIVATE)

    init {
        getContacts()
    }

    private fun getContacts() {
        sharedPreferences.getString(LOGIN_TOKEN, null)?.let { cookie ->
            viewModelScope.launch(dispatcher) {
                _contactsLiveData.apply {
                    postValue(Resource.Loading())
                    postValue(getContactsUseCase.getContacts(cookie))
                }
            }
        }
    }
}