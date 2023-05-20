package com.myproject.dietproject.domain.repository

import com.myproject.dietproject.domain.error.NetworkResult
import com.myproject.dietproject.domain.model.Kcal


interface KcalRepository {

    suspend fun getKcalData(descKor : String) : NetworkResult<List<Kcal>?>
}