package com.myproject.dietproject.domain.usecase

import com.google.firebase.storage.StorageReference
import com.myproject.dietproject.domain.repository.FirebaseRepository
import javax.inject.Inject

class AddUserProfileImage @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(userId: String, path: String) {

        repository.addProfileImage(userId, path)

    }

}