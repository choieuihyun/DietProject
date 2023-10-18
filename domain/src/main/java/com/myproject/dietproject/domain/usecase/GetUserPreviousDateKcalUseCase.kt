package com.myproject.dietproject.domain.usecase

import androidx.lifecycle.LiveData
import com.myproject.dietproject.domain.repository.FirebaseRepository
import javax.inject.Inject

class GetUserPreviousDateKcalUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(userId: String) {
        repository.getUserPreviousDateKcal(userId)
    }

    fun getPreviousDateKcal(): LiveData<Int> {
        return repository.todayKcal
    }

    fun getHomePreviousDateText(): LiveData<String> {
        return repository.homeDateText
    }

    fun getCalCulPreviousDateKcal(): Int {
        return repository.calculTodayKcal
    }

}