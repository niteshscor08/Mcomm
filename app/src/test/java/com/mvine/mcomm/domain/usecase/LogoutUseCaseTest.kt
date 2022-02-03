package com.mvine.mcomm.domain.usecase

import com.google.common.truth.Truth
import com.mvine.mcomm.domain.repository.LogoutRepository
import com.mvine.mcomm.util.EMPTY_STRING
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class LogoutUseCaseTest {

    private lateinit var logoutUseCase: LogoutUseCase
    private val logoutRepository: LogoutRepository = mockk()
    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        logoutUseCase = LogoutUseCase(logoutRepository)
    }

    @Test
    fun logout() {
        coEvery { logoutRepository.logout(any()) } returns mockk()
        dispatcher.runBlockingTest {
            val res = logoutRepository.logout(EMPTY_STRING)
            Truth.assertThat(res).isNotNull()
        }
    }
}
