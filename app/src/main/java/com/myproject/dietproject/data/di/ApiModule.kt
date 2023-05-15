package com.myproject.dietproject.data.di

import com.myproject.dietproject.data.db.remote.api.KcalApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun kcalApi(retrofit: Retrofit): KcalApi {
        return retrofit.create(KcalApi::class.java)
    }

}