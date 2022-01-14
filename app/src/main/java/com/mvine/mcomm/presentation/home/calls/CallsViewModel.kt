package com.mvine.mcomm.presentation.home.calls

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mvine.mcomm.data.model.response.PersonInfo
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.usecase.GetCallsUseCase
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.presentation.common.base.BaseViewModel
import com.mvine.mcomm.util.PreferenceHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallsViewModel @Inject constructor(
    private val getCallsUseCase: GetCallsUseCase,
    private val dispatcher: CoroutineDispatcher,
    private val preferenceHandler: PreferenceHandler
) : BaseViewModel() {

    private val _recentCallsLiveData: MutableLiveData<Resource<ArrayList<CallData>>> =
        MutableLiveData()
    val recentCalls: LiveData<Resource<ArrayList<CallData>>> = _recentCallsLiveData


    private val _userInfo: MutableLiveData<Resource<PersonInfo>> =
        MutableLiveData()
    val userInfo: LiveData<Resource<PersonInfo>> = _userInfo

    private val _searchCallsLiveData: MutableLiveData<ArrayList<CallData>> =
        MutableLiveData()
    val searchCalls: LiveData<ArrayList<CallData>> = _searchCallsLiveData

    init {
        getRecentCalls()
    }

    private fun getRecentCalls() {
            viewModelScope.launch(dispatcher) {
                _recentCallsLiveData.apply {
                    postValue(Resource.Loading())
                    postValue(getCallsUseCase.getRecentCalls())
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