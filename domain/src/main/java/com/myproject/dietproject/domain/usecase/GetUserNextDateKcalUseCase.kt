package com.myproject.dietproject.domain.usecase

import androidx.lifecycle.LiveData
import com.myproject.dietproject.domain.repository.FirebaseRepository
import javax.inject.Inject

class GetUserNextDateKcalUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(userId: String) {
        repository.getUserNextDateKcal(userId)
    }

    fun getNextDateKcal(): Int? {
        return repository.todayKcal.value
    }

    fun getHomeNextDateText(): String? {
        return repository.homeDateText.value
    }

    fun getCalCulNextDateKcal(): Int {
        return repository.calculTodayKcal
    }


}