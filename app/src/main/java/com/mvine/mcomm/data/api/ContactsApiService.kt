package com.mvine.mcomm.data.api

import com.mvine.mcomm.data.model.response.ContactsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * API service for Contact Requests
 */
interface ContactsApiService {

    @GET("call/callables")
    suspend fun getRecentContacts(@Header("Cookie") cookie: String): Response<ContactsResponse>
}