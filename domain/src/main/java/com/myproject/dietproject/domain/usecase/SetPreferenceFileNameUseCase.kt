package com.myproject.dietproject.domain.usecase

import com.myproject.dietproject.domain.repository.SharedPreferenceRepository
import javax.inject.Inject

class SetPreferenceFileNameUseCase @Inject constructor(
    private val repository: SharedPreferenceRepository
){

    suspend operator fun invoke(fileName: String) {
        repository.setSharedPreferenceFileName(fileName)
    }

}