package com.myproject.dietproject.data.datasource.localdatasource

import com.myproject.dietproject.data.db.local.db.FoodDiaryDatabase
import com.myproject.dietproject.data.db.local.entity.FoodDiaryEntity
import javax.inject.Inject

class FoodDiaryDataSource @Inject constructor(
    private val database: FoodDiaryDatabase
){

    fun getAllFoodDiary(): List<FoodDiaryEntity> {
        return database.kcalDao().getAllKcal()
    }

    suspend fun insertFoodDiary(foodDiary: FoodDiaryEntity) {
        database.kcalDao().insertKcal(foodDiary)
    }

    suspend fun deleteFoodDiary(foodDiary: FoodDiaryEntity) {
        database.kcalDao().deleteKcal(foodDiary)
    }


}