package com.mvine.mcomm.data.api

import com.mvine.mcomm.data.model.response.PersonInfo
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

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

    @GET("call/userview")
    suspend fun getUserView(@Header("Cookie") cookie: String): Response<PersonInfo>
}
