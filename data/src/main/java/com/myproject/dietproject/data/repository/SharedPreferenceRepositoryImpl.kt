package com.myproject.dietproject.data.repository

import com.myproject.dietproject.data.datasource.localdatasource.SharedPreferencesDataSource
import com.myproject.dietproject.domain.repository.SharedPreferenceRepository
import javax.inject.Inject

class SharedPreferenceRepositoryImpl @Inject constructor(
    private val dataSource: SharedPreferencesDataSource
): SharedPreferenceRepository {

    override suspend fun setSharedPreferenceFileName(fileName: String) {
        dataSource.setSharedPreferenceFileName(fileName)
    }

    override suspend fun getSharedPreferenceFavoriteState(key: String): String? {
        return dataSource.getSharedPreferenceValue(key)
    }

    override suspend fun addSharedPreferenceFavoriteState(key: String, value: String?) {
        dataSource.addSharedPreferenceValue(key, value)
    }

    override suspend fun deleteSharedPreferenceFavoriteState(key: String) {
        dataSource.deleteSharedPreferenceValue(key)
    }

}