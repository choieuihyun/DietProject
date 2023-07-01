package com.myproject.dietproject.domain.usecase

import com.myproject.dietproject.domain.repository.FirebaseRepository
import javax.inject.Inject

class AddUserInfoUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(userId: String,
                                name: String,
                                gender: String,
                                age: Int,
                                height: Float,
                                weight: Float,
                                targetWeight: Float,
                                recommendKcal: Int,
                                activity: String) {

        repository.addUserInfo(userId, name, gender, age, height, weight, targetWeight, recommendKcal, activity)

    }
}