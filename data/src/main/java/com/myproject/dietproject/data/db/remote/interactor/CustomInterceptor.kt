package com.myproject.dietproject.data.db.remote.interactor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class CustomInterceptor : Interceptor {
    // 쿼리 파라미터와 경로 변수의 차이 때문에 이렇게 된거
    // 서버에서 제공하는 규칙에 따라가야해서 만든 인터셉터

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