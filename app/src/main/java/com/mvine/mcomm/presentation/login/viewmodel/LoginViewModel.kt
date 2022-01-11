package com.mvine.mcomm.presentation.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mvine.mcomm.data.model.response.PersonInfo
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.domain.usecase.LoginUseCase
import com.mvine.mcomm.domain.util.Resource.*
import com.mvine.mcomm.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
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
): BaseViewModel() {

    private val _cookieLiveData: MutableLiveData<Resource<String?>> =
        MutableLiveData()
    val cookieLiveData: LiveData<Resource<String?>> = _cookieLiveData

    private val _userInfo: MutableLiveData<Resource<PersonInfo>> =
        MutableLiveData()
    val userInfo: LiveData<Resource<PersonInfo>> = _userInfo

    private val _hidePasswordErrorMsg : MutableLiveData<Boolean> = MutableLiveData(true)
     val hidePasswordErrorMsg: LiveData<Boolean> =
        _hidePasswordErrorMsg

    private val _hideEmailErrorMsg : MutableLiveData<Boolean> = MutableLiveData(true)
     val hideEmailErrorMsg: LiveData<Boolean> =
        _hideEmailErrorMsg

    fun login(username: String, password: String) {
        viewModelScope.launch(dispatcher) {
            with(_cookieLiveData) {
                postValue(Loading())
                postValue(loginUseCase.login(username, password))
            }
        }
    }

    fun getUserInfo(cookie: String){
            viewModelScope.launch(dispatcher) {
                _userInfo.apply {
                    postValue(loginUseCase.getUserInfo(cookie))
                }
            }
    }

    fun onPasswordTextChange(s: CharSequence, start: Int, before: Int, count: Int) {
       _hidePasswordErrorMsg.postValue(s.toString().length > PASSWORD_LENGTH)
    }

    fun onEmailTextChange(s: CharSequence, start: Int, before: Int, count: Int){
        _hideEmailErrorMsg.postValue(Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches())
    }

    companion object{
        const val PASSWORD_LENGTH = 5
    }

}