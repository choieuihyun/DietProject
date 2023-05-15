package com.myproject.dietproject.data.repository

import com.myproject.dietproject.data.datasource.remotedatasource.KcalDataSource
import com.myproject.dietproject.data.mapNetworkResult
import com.myproject.dietproject.data.toModel
import com.myproject.dietproject.domain.error.NetworkResult
import com.myproject.dietproject.domain.model.Kcal
import com.myproject.dietproject.domain.repository.KcalRepository
import javax.inject.Inject

class KcalRepositoryImpl @Inject constructor(
    private val dataSource: KcalDataSource
) : KcalRepository {

    override suspend fun getKcalData(descKor: String): NetworkResult<List<Kcal>?> {

        return dataSource.getKcalData(descKor).mapNetworkResult {
                kcalData -> kcalData?.map { // 이렇게 하는것보다 에러처리가 안전하지 않나?
                    it.toModel()
            }
        }
    }
}