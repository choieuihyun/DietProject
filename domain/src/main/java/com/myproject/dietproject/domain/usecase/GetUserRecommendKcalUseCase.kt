package com.myproject.dietproject.domain.usecase

import androidx.lifecycle.LiveData
import com.myproject.dietproject.domain.repository.FirebaseRepository
import javax.inject.Inject

class GetUserRecommendKcalUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(userId: String) {
        return repository.getUserRecommendKcal(userId)
    }

    fun getRecommendKcal(): LiveData<Int> {
        return repository.recommendKcal
    }

    fun getCalCulRecommendKcal(): Int {
        return repository.calculRecommendKcal
    }

    fun getScarceKcal(): LiveData<Int> {
        return repository.scarceKcal
    }

}