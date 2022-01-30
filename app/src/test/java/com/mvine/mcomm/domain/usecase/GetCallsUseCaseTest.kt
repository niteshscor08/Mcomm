package com.mvine.mcomm.domain.usecase

import com.google.common.truth.Truth
import com.mvine.mcomm.domain.repository.CallsRepository
import com.mvine.mcomm.domain.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class GetCallsUseCaseTest {
    private lateinit var getCallsUseCase: GetCallsUseCase
    private val callsRepository = mockk<CallsRepository>()
    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        getCallsUseCase = GetCallsUseCase(callsRepository)
        coEvery { callsRepository.getAllCalls() } returns Resource.Success(data = ArrayList())
        coEvery { callsRepository.getRecentCalls() } returns mockk()
    }

    @Test
    fun getAllCalls_test() {
        dispatcher.runBlockingTest {
            val res = getCallsUseCase.getAllCalls()
            Truth.assertThat(res.data).isNotNull()
        }
    }

    @Test
    fun getRecentCalls_test() {
        dispatcher.runBlockingTest {
            val res = getCallsUseCase.getRecentCalls()
            Truth.assertThat(res).isNotNull()
        }
    }
}
