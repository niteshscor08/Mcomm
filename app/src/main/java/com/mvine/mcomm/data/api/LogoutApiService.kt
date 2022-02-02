package com.mvine.mcomm.data.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * API service for Login Requests
 */
interface LogoutApiService {

    @GET("MVinelogout")
    suspend fun logout(@Header("Cookie") cookie: String): Response<ResponseBody>
}
