package com.myproject.dietproject.domain.error

sealed class NetworkError {

    object Unknown : NetworkError()
    object Timeout : NetworkError()
    object InternalServer : NetworkError()
    class BadRequest(val code: Int) : NetworkError()

}