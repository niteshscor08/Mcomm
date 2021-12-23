package com.mvine.mcomm.presentation.login.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.usecase.ChangePasswordUseCase
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.util.EMPTY_STRING
import com.mvine.mcomm.util.LOGIN_TOKEN
import com.mvine.mcomm.util.MCOMM_SHARED_PREFERENCES
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val dispatcher: CoroutineDispatcher,
    @ApplicationContext context: Context
): ViewModel(){

    var newPassword = EMPTY_STRING
    var reEnteredPassword = EMPTY_STRING


    private val _passwordUpdatedLiveData: MutableLiveData<Resource<String>> =
        MutableLiveData()
    val passwordUpdated: LiveData<Resource<String>> = _passwordUpdatedLiveData


    private val sharedPreferences =
        context.getSharedPreferences(MCOMM_SHARED_PREFERENCES, Context.MODE_PRIVATE)

    fun submitButtonClick(){
        sharedPreferences.getString(LOGIN_TOKEN, null)?.let { cookie ->
            viewModelScope.launch(dispatcher) {
                _passwordUpdatedLiveData.apply {
                    postValue(Resource.Loading())
                    postValue(changePasswordUseCase.changePassword(cookie, newPassword,reEnteredPassword))
                }
            }
        }
    }


}