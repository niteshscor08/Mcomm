package com.mvine.mcomm.presentation.home.calls

import androidx.lifecycle.ViewModel
import com.mvine.mcomm.domain.usecase.GetCallsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class CallsViewModel @Inject constructor(
    private val getCallsUseCase: GetCallsUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {
}