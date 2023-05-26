package com.myproject.dietproject.domain.usecase

import com.myproject.dietproject.domain.repository.FirebaseRepository
import java.util.Date
import javax.inject.Inject

class AddUserTodayKcalUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(userId: String, kcal: Float, foodName: String, date: Date) {
        repository.addTodayKcal(userId, kcal, foodName, date)
    }

}