package com.myproject.dietproject.domain.usecase

import com.myproject.dietproject.domain.repository.FirebaseRepository
import javax.inject.Inject

class GetUserPreviousDateKcalUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(userId: String) {
        repository.getUserPreviousDateKcal(userId)
    }

    fun getPreviousDateKcal(): Int? {
        return repository.todayKcal.value
    }

    fun getHomePreviousDateText(): String? {
        return repository.homeDateText.value
    }

    fun getCalCulPreviousDateKcal(): Int {
        return repository.calculTodayKcal
    }

}