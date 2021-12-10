package com.mvine.mcomm.presentation.home.chats

import androidx.lifecycle.ViewModel
import com.mvine.mcomm.domain.usecase.GetChatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val getChatsUseCase: GetChatsUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {
}