package com.myproject.dietproject.domain.usecase

import com.myproject.dietproject.data.db.remote.response.kcalresponse.User
import com.myproject.dietproject.domain.repository.FirebaseRepository
import javax.inject.Inject

class GetUserDB @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(userId: String, user: User) {
        repository.addUser(userId, user)
    }

}