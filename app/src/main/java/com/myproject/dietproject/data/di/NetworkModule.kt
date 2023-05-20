package com.myproject.dietproject.data.di

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.myproject.dietproject.data.datasource.remotedatasource.FirebaseDataSource
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

    @Singleton
    @Provides
    fun provideFirebaseRealtimeDB(): FirebaseDatabase {
        return Firebase.database("https://dietproject-386913-default-rtdb.asia-southeast1.firebasedatabase.app")
    }


}