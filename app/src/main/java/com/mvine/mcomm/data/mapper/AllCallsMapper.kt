package com.mvine.mcomm.data.mapper

import com.mvine.mcomm.data.model.response.CallablesResponse
import com.mvine.mcomm.domain.model.AllCalls
import com.mvine.mcomm.domain.model.AllCallsClass
import com.mvine.mcomm.domain.model.AllCallsCompanyData
import com.mvine.mcomm.domain.util.Resource

class AllCallsMapper {

    fun entityToModel(callsResponse: CallablesResponse?) : Resource<AllCalls> {

        val allCalls : AllCalls = AllCalls()
        val allCallsClassList = arrayListOf<AllCallsClass>()
        val allCallsCompanyData  = arrayListOf<AllCallsCompanyData>()

        callsResponse?.let {
            it.callables?.forEach { callable ->
                allCallsClassList.add(
                    AllCallsClass(
                        callable.Company,
                        callable.Department,
                        callable.STX,
                        callable.admin,
                        callable.companyid,
                        callable.email,
                        callable.id,
                        callable.image_src,
                        callable.usename,
                        callable.username
                    )
                )
            }
            it.companies?.forEach { company ->
                allCallsCompanyData.add(
                    AllCallsCompanyData(
                        company.ID,
                        company.Value,
                        company.image_src
                    )
                )
            }
        } ?: kotlin.run {
            return Resource.Error(message = "Error Fetching Calls Data")
        }
        return Resource.Success(data = allCalls)
    }

}