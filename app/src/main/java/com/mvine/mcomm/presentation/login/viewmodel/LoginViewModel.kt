package com.mvine.mcomm.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import com.mvine.mcomm.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * A View Model instance to help with the Login functionality
 *
 * @param loginUseCase The [LoginUseCase] instance
 * @param dispatcher The [CoroutineDispatcher] instance to off load time consuming calls off the main thread
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

}