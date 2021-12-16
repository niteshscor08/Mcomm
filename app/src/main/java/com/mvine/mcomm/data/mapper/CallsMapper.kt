package com.mvine.mcomm.data.mapper

import com.mvine.mcomm.data.model.response.CallsResponse
import com.mvine.mcomm.domain.model.CallData
import com.mvine.mcomm.domain.util.Resource
import com.mvine.mcomm.util.getSpinnerItems

class CallsMapper {

    fun entityToModel(callsResponse: CallsResponse?) : Resource<ArrayList<CallData>> {
        val recentCalls = arrayListOf<CallData>()
        callsResponse?.recents?.forEach { recent ->
            recentCalls.add(
                CallData(
                    dialstatus = recent.dialstatus,
                    image_src = recent.image_src,
                    othercaller_company = recent.othercaller_company,
                    othercaller_company_id = recent.othercaller_company_id,
                    othercaller_department = recent.othercaller_department,
                    othercaller_stx = recent.othercaller_stx,
                    timestamp = recent.timestamp,
                    type = recent.type,
                    callHistory = getSpinnerItems()
                )
            )
        } ?: kotlin.run {
            return Resource.Error(message = "Error Fetching Calls Data")
        }
        return Resource.Success(data = recentCalls)
    }
}