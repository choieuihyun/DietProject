package com.myproject.dietproject.domain.usecase

import com.myproject.dietproject.domain.model.KcalDataForCalendar
import com.myproject.dietproject.domain.repository.FirebaseRepository
import javax.inject.Inject

class GetUserCalendarDetailUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(userId: String, date: String) {
        repository.getCalendarDetailData(userId, date)
    }

    fun getDayKcalList(): ArrayList<KcalDataForCalendar?>? {
        return repository.dayKcalList.value
    }

    fun getDayKcal(): Int? {
        return repository.dayKcal.value
    }

}