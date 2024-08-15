package com.myproject.dietproject.domain.repository

import com.myproject.dietproject.domain.model.FoodDiaryModel
import kotlinx.coroutines.flow.Flow

interface FoodDiaryRepository {

    fun getAllFood(): Flow<List<FoodDiaryModel>?>

    suspend fun insertFood(foodDiaryModel: FoodDiaryModel)

    suspend fun deleteFood(foodName: String)

}