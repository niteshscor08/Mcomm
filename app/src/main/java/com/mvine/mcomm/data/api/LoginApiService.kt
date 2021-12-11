package com.mvine.mcomm.data.api

import okhttp3.FormBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * API service for Login Requests
 */
interface LoginApiService {

    @FormUrlEncoded
    @POST("MVinelogin")
    suspend fun login(
        @Field("credential_0") email: String,
        @Field("credential_1") password: String,
        @Field("destination") destination: String,
        @Field("Login") Login: String,
    ): Response<ResponseBody>
}