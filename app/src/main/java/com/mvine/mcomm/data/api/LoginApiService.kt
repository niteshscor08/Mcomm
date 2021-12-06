package com.mvine.mcomm.data.api

import com.mvine.mcomm.data.model.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * API service for Login Requests
 */
interface LoginApiService {

    @POST("MVinelogin")
    fun login(@Body loginRequest: LoginRequest)
}