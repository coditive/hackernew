package com.syrous.hackernews.remote

sealed class ApiResult<out T>(val response: T?, val success: Boolean, val message: String = "") {
    data class Success<out T>(val res: T) : ApiResult<T>(res, true)
    data class Error(val error: String) : ApiResult<Nothing>(null, false, error)
    data object NetworkError : ApiResult<Nothing>(
        response = null,
        success = false,
        message = "Unable to connect to the internet"
    )
}