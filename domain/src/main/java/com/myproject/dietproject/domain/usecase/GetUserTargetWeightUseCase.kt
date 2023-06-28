package com.myproject.dietproject.domain.usecase

import com.google.firebase.database.DatabaseReference
import com.myproject.dietproject.domain.repository.FirebaseRepository
import javax.inject.Inject

class GetUserTargetWeightUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(userId: String) : DatabaseReference {
        return repository.getUserTargetWeight(userId)
    }

}