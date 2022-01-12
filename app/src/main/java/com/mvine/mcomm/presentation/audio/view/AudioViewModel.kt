package com.mvine.mcomm.presentation.audio.view

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class AudioViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher
): ViewModel() {
}