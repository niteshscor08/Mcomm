package com.mvine.mcomm.domain.usecase

import com.google.common.truth.Truth
import com.mvine.mcomm.domain.repository.ChangePasswordRepository
import com.mvine.mcomm.util.EMPTY_STRING
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class ChangePasswordUseCaseTest {
    private lateinit var changePasswordUseCase: ChangePasswordUseCase
    private val changePasswordRepository = mockk<ChangePasswordRepository>()
    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        changePasswordUseCase = ChangePasswordUseCase(changePasswordRepository)
        coEvery { changePasswordRepository.changePassword(any(), any()) } returns mockk()
    }

    @Test
    fun changePassword_test() {
        dispatcher.runBlockingTest {
            val res = changePasswordUseCase.changePassword(EMPTY_STRING, EMPTY_STRING)
            Truth.assertThat(res).isNotNull()
        }
    }
}
