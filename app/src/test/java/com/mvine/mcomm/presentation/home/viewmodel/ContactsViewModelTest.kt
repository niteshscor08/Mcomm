package com.mvine.mcomm.presentation.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mvine.mcomm.domain.model.ContactsData
import com.mvine.mcomm.domain.usecase.GetContactsUseCase
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.presentation.home.contacts.ContactsViewModel
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ContactsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val dispatcher = TestCoroutineDispatcher()

    @RelaxedMockK
    private val getContactsUseCase: GetContactsUseCase = mockk()
    private lateinit var contactsViewModel: ContactsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        contactsViewModel = ContactsViewModel(
            getContactsUseCase,
            dispatcher
        )
        coEvery { getContactsUseCase.getContacts() } returns mockk()
    }

    @Test
    fun getContactTest() {
        dispatcher.runBlockingTest {
            contactsViewModel.getContacts()
            Assert.assertNotNull(contactsViewModel.contacts.value)
        }
    }
}