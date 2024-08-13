package com.myproject.dietproject.domain.usecase

import com.myproject.dietproject.domain.repository.SharedPreferenceRepository
import javax.inject.Inject

class AddSharedPreferenceFavoriteStateUseCase @Inject constructor(
    private val repository: SharedPreferenceRepository
) {

// 의존성 주입 -> 제어의 역전 -> 이런 의존성 주입 수동으로 해보기.

    suspend operator fun invoke(key: String, value: String?) {
        repository.addSharedPreferenceFavoriteState(key, value)
    }

}