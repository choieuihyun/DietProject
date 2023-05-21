package com.myproject.dietproject.domain.usecase

import com.myproject.dietproject.domain.repository.FirebaseRepository
import javax.inject.Inject

class GetDBTest @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(value: String) {
        repository.test(value)
    }

}