package com.mvine.mcomm.data.api

import com.mvine.mcomm.data.model.response.ChangePasswordResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ChangePasswordApiService {

    @POST("json/member/self/update_password")
    suspend fun changePassword(@Header("Cookie") cookie: String, @Body body: RequestBody): Response<ChangePasswordResponse>
}
