package com.mvine.mcomm.presentation.login.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth
import com.mvine.mcomm.domain.usecase.LogoutUseCase
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.util.PreferenceHandler
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginMenuViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val dispatcher = TestCoroutineDispatcher()
    private val preferenceHandler: PreferenceHandler = mockk()
    @RelaxedMockK
    private val logoutUseCase: LogoutUseCase = mockk()

    private lateinit var loginMenuViewModel: LoginMenuViewModel

    private var isLoggedOutTest: Observer<Resource<Boolean?>> = mockk()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        loginMenuViewModel = LoginMenuViewModel(preferenceHandler, dispatcher, logoutUseCase)
            .apply {
                isLoggedOut.observeForever(isLoggedOutTest)
            }
    }

    @Test
    fun logoutTest() {
        coEvery { logoutUseCase.logout(any()) } returns Resource.Success(data = true)
        every { preferenceHandler.getValue(any()) } returns ""
        every { isLoggedOutTest.onChanged(any()) } answers {}
        dispatcher.runBlockingTest {
            loginMenuViewModel.logout()
            verify { isLoggedOutTest.onChanged(any()) }
        }
    }

    @Test
    fun clearDataTest() {
        every { preferenceHandler.clearData() } returns mockk()
        dispatcher.runBlockingTest {
            loginMenuViewModel.clearData()
            verify { preferenceHandler.clearData() }
        }
    }

    @Test
    fun getUserDataTest() {
        every { preferenceHandler.get(any()) } returns mockk()
        dispatcher.runBlockingTest {
            Truth.assertThat(loginMenuViewModel.getUserData()).isNotNull()
        }
    }
}
