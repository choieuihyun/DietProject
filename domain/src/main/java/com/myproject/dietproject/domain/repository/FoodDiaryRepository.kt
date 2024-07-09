package com.myproject.dietproject.domain.repository

import com.myproject.dietproject.domain.model.FoodDiaryModel

interface FoodDiaryRepository {

    fun getAllFood(): List<FoodDiaryModel>

    suspend fun insertFood(foodDiaryModel: FoodDiaryModel)

    suspend fun deleteFood(foodDiaryModel: FoodDiaryModel)

}