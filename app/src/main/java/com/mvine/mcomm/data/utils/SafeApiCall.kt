package com.mvine.mcomm.data.utils

import com.mvine.mcomm.domain.util.Resource
import retrofit2.Response

suspend fun <T : Any> safeApiCall(
    call: suspend () -> Response<T>
): Resource<T> = try {
    val response = call.invoke()
    if (response.isSuccessful) {
        responseToResource(response)
    } else {
        Resource.Error(message = response.errorBody()?.string() ?: "Error Fetching Data")
    }
} catch (e: Exception) {
    Resource.Error(message = e.message ?: "Unknown Exception")
}

private fun <T : Any> responseToResource(
    response: Response<T>
): Resource<T> {
    return response.body()?.let { data ->
        Resource.Success(data)
    } ?: Resource.Error(response.message())
}
