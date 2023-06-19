package com.myproject.dietproject.domain.usecase

import com.google.firebase.database.DatabaseReference
import com.myproject.dietproject.domain.repository.FirebaseRepository
import javax.inject.Inject

class GetUserTodayKcalUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(userId: String, date: String) : DatabaseReference {
        return repository.getUserTodayKcal(userId, date)
    }

}