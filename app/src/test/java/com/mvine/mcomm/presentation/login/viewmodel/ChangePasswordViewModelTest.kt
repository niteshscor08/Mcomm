package com.mvine.mcomm.presentation.login.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.mvine.mcomm.domain.model.CredentialData
import com.mvine.mcomm.domain.usecase.ChangePasswordUseCase
import com.mvine.mcomm.presentation.readFile
import com.mvine.mcomm.util.CREDENTIAL_DATA
import com.mvine.mcomm.util.EMPTY_STRING
import com.mvine.mcomm.util.PreferenceHandler
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChangePasswordViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val dispatcher = TestCoroutineDispatcher()
    private val preferenceHandler: PreferenceHandler = mockk()
    @RelaxedMockK
    private val changePasswordUseCase: ChangePasswordUseCase = mockk()
    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    private val context = mockk<Context>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        changePasswordViewModel = ChangePasswordViewModel(
            changePasswordUseCase,
            dispatcher,
            preferenceHandler
        )
        coEvery { changePasswordUseCase.changePassword(any(), any()) } returns mockk()
        every { preferenceHandler.getValue(CREDENTIAL_DATA) } returns getCredentialData()
        every { preferenceHandler.save(any(), any()) } returns mockk()
    }

    @Test
    fun callPasswordUpdateAPITest() {
        dispatcher.runBlockingTest {
            changePasswordViewModel.callPasswordUpdateAPI()
            Assert.assertNotNull(changePasswordViewModel.passwordUpdated.value)
        }
    }

    @Test
    fun updatePasswordTest() {
        dispatcher.runBlockingTest {
            changePasswordViewModel.updatePassword(EMPTY_STRING)
            verify { preferenceHandler.save(any(), any()) }
        }
    }

    private fun getCredentialData(): String {
        return Gson().toJson(Gson().fromJson(readFile("userCredentialData.json", context), CredentialData::class.java))
    }
}
