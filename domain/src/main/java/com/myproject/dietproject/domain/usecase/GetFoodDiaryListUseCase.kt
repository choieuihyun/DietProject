package com.myproject.dietproject.domain.usecase

import com.myproject.dietproject.domain.model.FoodDiaryModel
import com.myproject.dietproject.domain.repository.FoodDiaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFoodDiaryListUseCase @Inject constructor(private val repository: FoodDiaryRepository) {

    operator fun invoke(): Flow<List<FoodDiaryModel>?> {
        return repository.getAllFood()
    }

}