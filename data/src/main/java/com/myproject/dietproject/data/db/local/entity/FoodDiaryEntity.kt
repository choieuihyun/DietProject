package com.myproject.dietproject.data.db.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "foodDiary")
data class FoodDiaryEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // 이렇게 해둬도 autoGenerate를 설정해두었기 때문에 알아서 올라감.
    val kcal: Float?,
    val foodName: String?,
    val makerName: String?,
    val favoriteButtonState: Boolean

) {

//    // 여기서는 필요없는거 아녀?
//    fun toggleFavorite() {
//        buttonState = !buttonState
//    }

}


