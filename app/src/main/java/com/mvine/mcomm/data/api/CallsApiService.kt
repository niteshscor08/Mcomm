package com.mvine.mcomm.data.api

import com.mvine.mcomm.data.model.response.CallablesResponse
import com.mvine.mcomm.data.model.response.CallsResponse
import com.mvine.mcomm.data.model.response.PersonInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * API service for Call Requests
 */
interface CallsApiService {

    @GET("call/recents")
    suspend fun getRecentCalls(@Header("Cookie") cookie: String): Response<CallsResponse>

    @GET("call/callables")
    suspend fun getAllCalls(@Header("Cookie") cookie: String): Response<CallablesResponse>

    @GET("call/userview")
    suspend fun getUserView(@Header("Cookie") cookie: String): Response<PersonInfo>

}