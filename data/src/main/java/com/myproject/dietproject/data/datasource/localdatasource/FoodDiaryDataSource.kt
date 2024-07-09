package com.myproject.dietproject.data.datasource.localdatasource

import android.util.Log
import androidx.lifecycle.LiveData
import com.myproject.dietproject.data.db.local.db.FoodDiaryDatabase
import com.myproject.dietproject.data.db.local.entity.FoodDiaryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class FoodDiaryDataSource @Inject constructor(
    private val database: FoodDiaryDatabase
){

    fun getAllFoodDiary(): Flow<List<FoodDiaryEntity>?> {
        return database.kcalDao().getAllKcal()
    }

    suspend fun insertFoodDiary(foodDiary: FoodDiaryEntity) {
        database.kcalDao().insertKcal(foodDiary)
    }

    suspend fun deleteFoodDiary(foodName: String) {
        database.kcalDao().deleteKcal(foodName)
    }


}