package com.mvine.mcomm.domain.usecase

import com.google.common.truth.Truth
import com.mvine.mcomm.domain.repository.LoginRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {
    private lateinit var loginUseCase: LoginUseCase
    private val loginRepository = mockk<LoginRepository>()
    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        loginUseCase = LoginUseCase(loginRepository)
    }

    @Test
    fun login_test() {
        coEvery { loginRepository.login(any(), any()) } returns mockk()
        dispatcher.runBlockingTest {
            val res = loginUseCase.login(USERNAME, PASSWORD)
            Truth.assertThat(res).isNotNull()
        }
    }

    @Test
    fun getUserInfo_test() {
        coEvery { loginRepository.getUserInfo(any()) } returns mockk()
        dispatcher.runBlockingTest {
            val res = loginUseCase.getUserInfo(USERNAME)
            Truth.assertThat(res).isNotNull()
        }
    }

    companion object {
        const val USERNAME = "username"
        const val PASSWORD = "password"
    }
}
