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

    fun getNextDateKcal(): LiveData<Int> {
        return repository.todayKcal
    }

    fun getHomeNextDateText(): LiveData<String> {
        return repository.homeDateText
    }

    fun getCalCulNextDateKcal(): Int {
        return repository.calculTodayKcal
    }


}