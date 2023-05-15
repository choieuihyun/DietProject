package com.myproject.dietproject.data.di

import com.myproject.dietproject.data.db.remote.interactor.NetworkErrorHandlerImpl
import com.myproject.dietproject.domain.error.NetworkErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val KcalAPI_BASE_URL = "http://openapi.foodsafetykorea.go.kr/" // 식품영양정보 DB

    @Singleton
    @Provides
    fun provideOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(CustomInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit {

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(KcalAPI_BASE_URL)
            .build()

    }

    @Singleton
    @Provides
    fun provideNetworkHandler(): NetworkErrorHandler {
        return NetworkErrorHandlerImpl()
    }


}