package com.mvine.mcomm.presentation.home.contacts

import androidx.lifecycle.ViewModel
import com.mvine.mcomm.domain.usecase.GetContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {
}