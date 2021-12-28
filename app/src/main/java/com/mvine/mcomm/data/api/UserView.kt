package com.mvine.mcomm.data.api

import com.mvine.mcomm.data.model.response.PersonInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UserView {
    @GET("call/userview")
    suspend fun getUserView(@Header("Cookie") cookie: String): Response<PersonInfo>
}