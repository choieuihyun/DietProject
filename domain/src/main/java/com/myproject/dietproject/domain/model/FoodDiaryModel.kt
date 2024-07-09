package com.myproject.dietproject.domain.model


data class FoodDiaryModel (

    val id: Int = 0,
    val kcal: Float?,
    val foodName: String?,
    val makerName: String?,
    val favoriteButtonState: Boolean

    ) {

}
