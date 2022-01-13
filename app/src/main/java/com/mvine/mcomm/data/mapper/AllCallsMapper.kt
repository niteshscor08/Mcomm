package com.mvine.mcomm.data.mapper

import com.mvine.mcomm.data.model.response.CallablesResponse
import com.mvine.mcomm.domain.model.AllCalls
import com.mvine.mcomm.domain.model.AllCallsClass
import com.mvine.mcomm.domain.model.AllCallsCompanyData
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.util.getSpinnerItems

class AllCallsMapper {

    fun entityToModel(callsResponse: CallablesResponse?) : Resource<ArrayList<CallData>> {
        val allCalls = arrayListOf<CallData>()
        callsResponse?.let {
            it.callables?.forEach { callable ->
                allCalls.add(
                    CallData(
                        dialstatus = null,
                        image_src = callable.image_src,
                        othercaller_company = callable.Company,
                        othercaller_company_id = callable.companyid,
                        othercaller_department = callable.Department,
                        othercaller_stx = callable.STX,
                        timestamp = null,
                        type = null,
                        callHistory = getSpinnerItems(),
                    )
                )
            }
        } ?: kotlin.run {
            return Resource.Error(message = "Error Fetching Calls Data")
        }
        return Resource.Success(data = allCalls)
    }

}