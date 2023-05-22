package com.myproject.dietproject.data.di

import com.myproject.dietproject.data.datasource.remotedatasource.FirebaseDataSource
import com.myproject.dietproject.data.datasource.remotedatasource.KcalDataSource
import com.myproject.dietproject.data.repository.FirebaseRepositoryImpl
import com.myproject.dietproject.data.repository.KcalRepositoryImpl
import com.myproject.dietproject.domain.repository.FirebaseRepository
import com.myproject.dietproject.domain.repository.KcalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideKcalRepository(dataSource: KcalDataSource) : KcalRepository {
        return KcalRepositoryImpl(dataSource)
    }

    @Singleton
    @Provides
    fun provideFirebaseDB(dataSource: FirebaseDataSource) : FirebaseRepository {
        return FirebaseRepositoryImpl(dataSource)
    }
}