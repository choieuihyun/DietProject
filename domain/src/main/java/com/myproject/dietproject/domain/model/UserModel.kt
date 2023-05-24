package com.myproject.dietproject.domain.model

import androidx.room.PrimaryKey
import java.util.Date
import java.io.Serializable

data class UserModel (

    @PrimaryKey val uid: String = "", // firebase uid
    @PrimaryKey val email: String = "", // 이메일
    val gender: String = "",
    val age: Int = 0, // 나이
    val height: Float = 0.0F, // 키
    val weight: Float = 0.0F, // 몸무게
    val activity: String = "", // 활동량, 1(가벼움), 2(중간), 3(많음)
    val todayKcal: TodayKcal? = null // 오늘 먹은 칼로리
) : Serializable


data class TodayKcal ( // 하루에 먹은 칼로리

    val kcal: Float,
    val foodName: String,
    val date: Date

)