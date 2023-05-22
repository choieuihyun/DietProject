package com.myproject.dietproject.data.di

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class CustomInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        var stringurl = request.url.toString()

        stringurl = stringurl.replace("&=", "/")
        stringurl = stringurl.replace("?=", "")

        val newRequest: Request = Request.Builder()
            .url(stringurl)
            .build()
        return chain.proceed(newRequest)
    }

}