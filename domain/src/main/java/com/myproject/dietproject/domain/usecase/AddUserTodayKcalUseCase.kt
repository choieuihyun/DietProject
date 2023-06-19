package com.myproject.dietproject.domain.usecase

import com.myproject.dietproject.domain.repository.FirebaseRepository
import javax.inject.Inject

class AddUserTodayKcalUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(userId: String, kcal: Float, foodName: String, makerName: String, date: String) {
        repository.addTodayKcal(userId, kcal, foodName, makerName, date)
    }

}