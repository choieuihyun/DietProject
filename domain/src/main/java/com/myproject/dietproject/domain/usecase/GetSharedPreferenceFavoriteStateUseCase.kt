package com.myproject.dietproject.domain.usecase

import com.myproject.dietproject.domain.repository.SharedPreferenceRepository
import javax.inject.Inject

class GetSharedPreferenceFavoriteStateUseCase @Inject constructor(
    private val repository: SharedPreferenceRepository
) {

    suspend operator fun invoke(key: String): String? {

        return repository.getSharedPreferenceFavoriteState(key)

    }

}