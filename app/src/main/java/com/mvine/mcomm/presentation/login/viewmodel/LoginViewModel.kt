package com.mvine.mcomm.presentation.login.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mvine.mcomm.data.model.response.PersonInfo
import com.mvine.mcomm.domain.model.CredentialData
import com.mvine.mcomm.domain.usecase.LoginUseCase
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.domain.util.Resource.*
import com.mvine.mcomm.presentation.common.base.BaseViewModel
import com.mvine.mcomm.util.*
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
    private val dispatcher: CoroutineDispatcher,
    private val preferenceHandler: PreferenceHandler
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

    fun checkTokenValidity(token : String?): Boolean{
         if(token.isNullOrEmpty()){
             return false
         }
        val tokenTime: Long = extractEpochTime(token.substringAfter(TIME_EXTRACTOR))
        val currentTime: Long = getSubStringBasedOnIndex(System.currentTimeMillis().toString(), tokenTime.toString().length ).toLong()
        val timeDifference = currentTime - tokenTime
        if(timeDifference >= TIME_DIFF ){
            return false
        }
        return true
    }

    fun saveCredentialData(username: String?, password: String?, token: String?, isRefresh : Boolean?= false){
        val credentialData = CredentialData(
            userName = username,
            password = password,
            token = token,
            isRefresh = isRefresh
        )
        preferenceHandler.save(LOGIN_TOKEN, token)
        preferenceHandler.save( CREDENTIAL_DATA,
            Gson().toJson(credentialData)
        )
    }

    fun getCredentialData(): CredentialData {
      preferenceHandler.getValue(CREDENTIAL_DATA)?.let {
          return   Gson().fromJson(it, CredentialData::class.java)
      }?:  return CredentialData()
    }

    fun clearSavedUserInformation(){
        preferenceHandler.clearData()
    }

    companion object{
        const val PASSWORD_LENGTH = 5
        const val TIME_EXTRACTOR = "time:"
        const val TIME_DIFF = 14040L
    }

}