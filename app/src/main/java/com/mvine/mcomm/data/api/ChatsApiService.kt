package com.mvine.mcomm.data.api

import com.mvine.mcomm.data.model.LoginRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * API service for Chat Requests
 */
interface ChatsApiService {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("MVinelogin")
    suspend fun login(@Body loginRequest: LoginRequest): Response<ResponseBody>
}
