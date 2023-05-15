package com.myproject.dietproject.domain.usecase

import com.myproject.dietproject.domain.error.NetworkResult
import com.myproject.dietproject.domain.model.Kcal
import com.myproject.dietproject.domain.repository.KcalRepository
import javax.inject.Inject

class GetKcalDataUseCase @Inject constructor(
    private val kcalDataRepository: KcalRepository
){

    suspend operator fun invoke(descKor : String) : NetworkResult<List<Kcal>?> {
        return kcalDataRepository.getKcalData(descKor)
    }

}