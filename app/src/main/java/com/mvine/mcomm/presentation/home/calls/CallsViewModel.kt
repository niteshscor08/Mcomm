package com.mvine.mcomm.presentation.home.calls

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvine.mcomm.data.model.response.PersonInfo
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.usecase.GetCallsUseCase
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.util.LOGIN_TOKEN
import com.mvine.mcomm.util.MCOMM_SHARED_PREFERENCES
import com.mvine.mcomm.util.USER_INFO
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallsViewModel @Inject constructor(
    private val getCallsUseCase: GetCallsUseCase,
    private val dispatcher: CoroutineDispatcher,
    @ApplicationContext context: Context
) : ViewModel() {

    private val _recentCallsLiveData: MutableLiveData<Resource<ArrayList<CallData>>> =
        MutableLiveData()
    val recentCalls: LiveData<Resource<ArrayList<CallData>>> = _recentCallsLiveData


    private val _userInfo: MutableLiveData<Resource<PersonInfo>> =
        MutableLiveData()
    val userInfo: LiveData<Resource<PersonInfo>> = _userInfo

    private val _searchCallsLiveData: MutableLiveData<ArrayList<CallData>> =
        MutableLiveData()
    val searchCalls: LiveData<ArrayList<CallData>> = _searchCallsLiveData

    private val sharedPreferences =
        context.getSharedPreferences(MCOMM_SHARED_PREFERENCES, Context.MODE_PRIVATE)

    init {
        getRecentCalls()
        //getUserInfo()
    }

    private fun getUserInfo(){
        sharedPreferences.getString(LOGIN_TOKEN,null)?.let { cookie ->
            viewModelScope.launch(dispatcher) {
                _userInfo.apply {
                    postValue(getCallsUseCase.getUserInfo(cookie))
                }
            }
        }
    }

    private fun getRecentCalls() {
        sharedPreferences.getString(LOGIN_TOKEN, null)?.let { cookie ->
            viewModelScope.launch(dispatcher) {
                _recentCallsLiveData.apply {
                    postValue(Resource.Loading())
                    postValue(getCallsUseCase.getRecentCalls(cookie))
                }
            }
        }
    }

    fun filterData(query: String) {
        _recentCallsLiveData.value?.data?.filter {
            it.othercaller_company_id?.contains(query, ignoreCase = true) == true
        }?.let {
            _searchCallsLiveData.postValue(it as ArrayList<CallData>)
        }
    }
}