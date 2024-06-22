package com.myproject.dietproject.data.repository

import android.util.Log
import com.myproject.dietproject.data.datasource.localdatasource.FoodDiaryDataSource
import com.myproject.dietproject.data.db.local.db.FoodDiaryDatabase
import com.myproject.dietproject.data.db.local.entity.FoodDiaryEntity
import com.myproject.dietproject.data.mapper.toEntity
import com.myproject.dietproject.data.mapper.toModel
import com.myproject.dietproject.domain.model.FoodDiaryModel
import com.myproject.dietproject.domain.repository.FoodDiaryRepository
import java.lang.Exception
import javax.inject.Inject


class FoodDiaryRepositoryImpl @Inject constructor(
    private val dataSource: FoodDiaryDataSource
): FoodDiaryRepository{

    override fun getAllFood(): List<FoodDiaryModel> {
        return try {
            dataSource.getAllFoodDiary().map {
                it.toModel()
            }
        } catch (e: Exception) {
            Log.e("RoomDBError", "RoomDBSelectError")
            emptyList<FoodDiaryModel>()
        }
    }

    override suspend fun insertFood(foodDiaryModel: FoodDiaryModel) {
        dataSource.insertFoodDiary(foodDiaryModel.toEntity())
    }

    override suspend fun deleteFood(foodDiaryModel: FoodDiaryModel) {
        dataSource.deleteFoodDiary(foodDiaryModel.toEntity())
    }



}