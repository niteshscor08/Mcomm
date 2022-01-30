package com.mvine.mcomm.domain.usecase

import com.google.common.truth.Truth
import com.mvine.mcomm.domain.model.ContactsData
import com.mvine.mcomm.domain.repository.ContactsRepository
import com.mvine.mcomm.domain.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class GetContactsUseCaseTest {
    private lateinit var getContactsUseCase: GetContactsUseCase
    private val contactsRepository = mockk<ContactsRepository>()
    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        getContactsUseCase = GetContactsUseCase(contactsRepository)
    }

    @Test
    fun login_test() {
        val responseData = Resource.Success(data = ArrayList<ContactsData>())
        coEvery { contactsRepository.getContacts() } returns responseData
        dispatcher.runBlockingTest {
            val res = getContactsUseCase.getContacts()
            Truth.assertThat(res.data).isNotNull()
        }
    }
}
