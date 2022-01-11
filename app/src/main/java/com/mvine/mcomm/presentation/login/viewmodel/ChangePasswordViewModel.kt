package com.mvine.mcomm.presentation.login.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvine.mcomm.domain.usecase.ChangePasswordUseCase
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.util.EMPTY_STRING
import com.mvine.mcomm.util.LOGIN_TOKEN
import com.mvine.mcomm.util.PreferenceHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val dispatcher: CoroutineDispatcher,
    private val preferenceHandler: PreferenceHandler
    ): ViewModel(){

    var newPassword = EMPTY_STRING
    var reEnteredPassword = EMPTY_STRING


    private val _passwordUpdatedLiveData: MutableLiveData<Resource<String>> =
        MutableLiveData()
    val passwordUpdated: LiveData<Resource<String>> = _passwordUpdatedLiveData

    fun submitButtonClick(){
        preferenceHandler.getValue(LOGIN_TOKEN)?.let { cookie ->
            viewModelScope.launch(dispatcher) {
                _passwordUpdatedLiveData.apply {
                    postValue(Resource.Loading())
                    postValue(changePasswordUseCase.changePassword(cookie, newPassword,reEnteredPassword))
                }
            }
        }
    }


}