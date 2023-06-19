package com.myproject.dietproject.data.datasource.remotedatasource

import com.myproject.dietproject.data.BuildConfig
import com.myproject.dietproject.data.db.remote.api.KcalApi
import com.myproject.dietproject.data.db.remote.response.kcalresponse.KcalData
import com.myproject.dietproject.domain.error.NetworkErrorHandler
import com.myproject.dietproject.domain.error.NetworkResult
import javax.inject.Inject

class KcalDataSource @Inject constructor(
    private val api: KcalApi,
    private val networkErrorHandler: NetworkErrorHandler
) {

    suspend fun getKcalData(descKor: String) : NetworkResult<List<KcalData>?> {

        val response = api.getKcalData(
            BuildConfig.KCAL_API_KEY, "I2790", "json","1","1",
            "DESC_KOR=$descKor")

        return try {
            NetworkResult.Success(response.body()?.i2790?.row)
        } catch (exception : Exception) {
            val errorType = networkErrorHandler.getError(exception)
            NetworkResult.Error(errorType)
        }

    }

}