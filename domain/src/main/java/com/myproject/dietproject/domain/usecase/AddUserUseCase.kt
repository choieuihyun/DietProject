package com.myproject.dietproject.domain.usecase

import com.myproject.dietproject.domain.model.UserModel
import com.myproject.dietproject.domain.repository.FirebaseRepository
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(userId: String, userEmail: String) {
        repository.addUser(userId, userEmail)
    }

}