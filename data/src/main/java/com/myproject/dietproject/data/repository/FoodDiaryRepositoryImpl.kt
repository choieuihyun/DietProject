package com.myproject.dietproject.data.repository

import android.util.Log
import com.myproject.dietproject.data.datasource.localdatasource.FoodDiaryDataSource
import com.myproject.dietproject.data.mapper.toEntity
import com.myproject.dietproject.data.mapper.toModel
import com.myproject.dietproject.domain.model.FoodDiaryModel
import com.myproject.dietproject.domain.repository.FoodDiaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class FoodDiaryRepositoryImpl @Inject constructor(
    private val dataSource: FoodDiaryDataSource
): FoodDiaryRepository{

    override fun getAllFood(): Flow<List<FoodDiaryModel>?> {
        return flow {
            dataSource.getAllFoodDiary().collect() { entities ->
                val modelList = entities?.map { entity ->
                    entity.toModel()
                } ?: emptyList()
                emit(modelList)
            }
        }.flowOn(Dispatchers.IO).catch { e ->
            emit(emptyList())
        }
    }

    override suspend fun insertFood(foodDiaryModel: FoodDiaryModel) {
        dataSource.insertFoodDiary(foodDiaryModel.toEntity())
    }

    override suspend fun deleteFood(foodName: String) {
        dataSource.deleteFoodDiary(foodName)
    }



}