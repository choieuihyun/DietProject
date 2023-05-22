package com.myproject.dietproject.domain.error

interface NetworkErrorHandler {

    fun getError(exception: Throwable): NetworkError

}