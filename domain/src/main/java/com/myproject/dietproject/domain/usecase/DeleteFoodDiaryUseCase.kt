package com.myproject.dietproject.domain.usecase

import com.myproject.dietproject.domain.repository.FoodDiaryRepository
import javax.inject.Inject

class DeleteFoodDiaryUseCase @Inject constructor(
    private val repository: FoodDiaryRepository
) {

    suspend operator fun invoke(foodName: String) {
        repository.deleteFood(foodName)
    }

}