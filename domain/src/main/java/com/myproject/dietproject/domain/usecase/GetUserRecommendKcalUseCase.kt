package com.myproject.dietproject.domain.usecase

import com.myproject.dietproject.domain.repository.FirebaseRepository
import javax.inject.Inject

class GetUserRecommendKcalUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(userId: String) {
        return repository.getUserRecommendKcal(userId)
    }

    fun getRecommendKcal(): Int? {
        return repository.recommendKcal.value
    }

    fun getCalCulRecommendKcal(): Int {
        return repository.calculRecommendKcal
    }

}