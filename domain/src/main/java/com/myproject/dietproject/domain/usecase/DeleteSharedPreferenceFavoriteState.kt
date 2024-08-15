package com.myproject.dietproject.domain.usecase

import com.myproject.dietproject.domain.repository.SharedPreferenceRepository
import javax.inject.Inject

class DeleteSharedPreferenceFavoriteState @Inject constructor(
    private val repository: SharedPreferenceRepository
) {

    suspend operator fun invoke(key: String) {
        repository.deleteSharedPreferenceFavoriteState(key)
    }

}