package com.myproject.dietproject.domain.usecase

import com.myproject.dietproject.domain.model.FoodDiaryModel
import com.myproject.dietproject.domain.repository.FoodDiaryRepository
import javax.inject.Inject

class AddFoodDiaryListUseCase @Inject constructor(
    private val repository: FoodDiaryRepository
) {

    suspend operator fun invoke(foodDiaryModel: FoodDiaryModel) {
        repository.insertFood(foodDiaryModel)
    }

}