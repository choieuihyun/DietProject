package com.myproject.dietproject.domain.usecase

import com.myproject.dietproject.domain.model.FoodDiaryModel
import com.myproject.dietproject.domain.repository.FoodDiaryRepository
import javax.inject.Inject

class GetFoodDiaryListUseCase @Inject constructor(private val repository: FoodDiaryRepository) {

    operator fun invoke(): List<FoodDiaryModel> {
        return repository.getAllFood()
    }

}