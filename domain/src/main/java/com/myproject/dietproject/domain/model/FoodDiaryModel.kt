package com.myproject.dietproject.domain.model

import java.io.Serializable


data class FoodDiaryModel (
    
    val id: Int = 0, // id는 필요가 없구나
    val kcal: Float?,
    val foodName: String?,
    val makerName: String?,
    val carbonHydrate: String?,
    val protein: String?,
    val fat: String?,
    var favoriteButtonState: Boolean

    ): Serializable {

}
