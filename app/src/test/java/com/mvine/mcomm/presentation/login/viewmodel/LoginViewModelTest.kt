package com.mvine.mcomm.presentation.login.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.util.PatternsCompat
import androidx.lifecycle.Observer
import com.google.common.truth.ExpectFailure.assertThat
import com.google.common.truth.Truth
import com.google.gson.Gson
import com.mvine.mcomm.domain.model.CredentialData
import com.mvine.mcomm.domain.usecase.LoginUseCase
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.presentation.readFile
import com.mvine.mcomm.util.CREDENTIAL_DATA
import com.mvine.mcomm.util.PreferenceHandler
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkObject
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString

class LoginViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // creating test dispatcher
    private val dispatcher = TestCoroutineDispatcher()
    // mocking use case instance
    @RelaxedMockK
    private val loginUseCase: LoginUseCase = mockk()
    private val preferenceHandler: PreferenceHandler = mockk()
    private lateinit var loginViewModel: LoginViewModel
    private val context = mockk<Context>()

    private var cookieLiveDataTest: Observer<Resource<String?>> = mockk()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        loginViewModel = LoginViewModel(
            loginUseCase, dispatcher, preferenceHandler
        ).apply {
            cookieLiveData.observeForever(cookieLiveDataTest)
        }
    }

    @Test
    fun testNull() {
        coEvery { loginUseCase.login(anyString(), anyString()) } returns Resource.Success(data = null)
        dispatcher.runBlockingTest {
            assertNotNull(loginViewModel.login(anyString(), anyString()))
            assertTrue(loginViewModel.cookieLiveData.hasObservers())
        }
    }

    @Test
    fun loginTest() {
        coEvery { loginUseCase.login(anyString(), anyString()) } returns Resource.Success(data = "CookiesData")
        every { cookieLiveDataTest.onChanged(any()) } answers {}
        dispatcher.runBlockingTest {
            loginViewModel.login(anyString(), anyString())
            Truth.assertThat(loginViewModel.cookieLiveData.value).isNotNull()
        }
    }

    @Test
    fun onPasswordTextChangeFailTest() {
        dispatcher.runBlockingTest {
            loginViewModel.onPasswordTextChange("ABC", anyInt(), anyInt(), anyInt())
            Truth.assertThat(loginViewModel.hidePasswordErrorMsg.value).isEqualTo(false)
        }
    }

    @Test
    fun onPasswordTextChangePassTest() {
        dispatcher.runBlockingTest {
            loginViewModel.onPasswordTextChange("ABCDEF", anyInt(), anyInt(), anyInt())
            Truth.assertThat(loginViewModel.hidePasswordErrorMsg.value).isEqualTo(true)
        }
    }

    @Test
    fun onEmailTextChangeFail() {
        dispatcher.runBlockingTest {
            mockkObject(PatternsCompat.EMAIL_ADDRESS)
            every { PatternsCompat.EMAIL_ADDRESS.matcher(any()).matches() } returns true
            loginViewModel.onEmailTextChange("email@mvine.com", anyInt(), anyInt(), anyInt())
            Truth.assertThat(loginViewModel.hideEmailErrorMsg.value).isEqualTo(true)
        }
    }

    @Test
    fun updateTokenAndLoginWithOldTokenTest() {
        dispatcher.runBlockingTest {
            coEvery { loginUseCase.getUserInfo(any()) } returns mockk()
            every { preferenceHandler.getValue(CREDENTIAL_DATA) } returns getCredentialData()
            every { preferenceHandler.save(any(), any()) } returns mockk()
            loginViewModel.updateTokenAndLogin(anyString(), anyString(), anyString())
            Truth.assertThat(loginViewModel.userInfo.value).isNotNull()
        }
    }

    @Test
    fun updateTokenAndLoginWithNewTokenTest() {
        dispatcher.runBlockingTest {
            val response = Gson().toJson(mockk<CredentialData>())
            coEvery { loginUseCase.getUserInfo(any()) } returns mockk()
            every { preferenceHandler.getValue(CREDENTIAL_DATA) } returns response
            every { preferenceHandler.save(any(), any()) } returns mockk()
            loginViewModel.updateTokenAndLogin(anyString(), anyString(), anyString())
            Truth.assertThat(loginViewModel.userInfo.value).isNotNull()
        }
    }

    private fun getCredentialData(): String {
        return Gson().toJson(Gson().fromJson(readFile("userCredentialData.json", context), CredentialData::class.java))
    }
}
