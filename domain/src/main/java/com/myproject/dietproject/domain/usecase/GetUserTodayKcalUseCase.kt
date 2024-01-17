package com.myproject.dietproject.domain.usecase

import androidx.lifecycle.LiveData
import com.google.firebase.database.DatabaseReference
import com.myproject.dietproject.domain.repository.FirebaseRepository
import javax.inject.Inject

class GetUserTodayKcalUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(userId: String) {
        return repository.getUserTodayKcal(userId)
    }

    fun getTodayKcal(): Int? { // 이거 그냥 일반데이터로 해야될 것 같은데? value로 받아와서
        return repository.todayKcal.value
    }

    fun getHomeDateText(): String? {
        return repository.homeDateText.value
    }

    fun getDateTextByDate(): String? {
        return repository.homeDateTextByDate.value
    }

    fun getCalCulTodayKcal() : Int {
        return repository.calculTodayKcal
    }


}