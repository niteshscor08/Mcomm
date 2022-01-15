package com.mvine.mcomm.presentation.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mvine.mcomm.data.model.response.PersonInfo
import com.mvine.mcomm.domain.model.CredentialData
import com.mvine.mcomm.domain.usecase.LogoutUseCase
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.presentation.common.base.BaseViewModel
import com.mvine.mcomm.util.LOGIN_TOKEN
import com.mvine.mcomm.util.PreferenceHandler
import com.mvine.mcomm.util.USER_INFO
import com.mvine.mcomm.util.getCredentials
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginMenuViewModel @Inject constructor(
    private val preferenceHandler: PreferenceHandler,
    private val dispatcher: CoroutineDispatcher,
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel(){

    private val _isLoggedOut: MutableLiveData<Resource<Boolean?>> =
        MutableLiveData()
    val isLoggedOut: LiveData<Resource<Boolean?>> = _isLoggedOut

    fun logout(){
        preferenceHandler.getValue(LOGIN_TOKEN)?.let { cookie ->
            viewModelScope.launch(dispatcher) {
                with(_isLoggedOut) {
                    postValue(Resource.Loading())
                    postValue(logoutUseCase.logout(cookie))
                }
            }
        }
    }

    fun clearData(){
        preferenceHandler.clearData()
    }

    fun getUserData(): PersonInfo {
        return preferenceHandler.get(USER_INFO)
    }
}