package com.myproject.dietproject.data.db.remote.firebase.userdata

import androidx.room.PrimaryKey
import java.util.Date

data class User (

    @PrimaryKey val uid: String = "", // firebase uid
    @PrimaryKey val email: String = "", // 이메일
    val name: String = "", // 이름 or 닉네임
    val gender: String = "", // 성별
    val age: Int = 0, // 나이
    val height: Float = 0.0F, // 키
    val weight: Float = 0.0F, // 몸무게
    val targetWeight: Float = 0.0F, // 목표 체중
    val activity: String = "", // 활동량, 1(가벼움), 2(중간), 3(많음)
    val recommendKcal: Float = 0.0F, // 권장 칼로리
    val overKcal: Int = 0, // 권장 칼로리 초과 횟수
    val todayKcal: TodayKcal? = null // 오늘 먹은 칼로리

)

data class TodayKcal ( // 하루에 먹은 칼로리

    val kcal: Float,
    val foodName: String,
    val date: Date

        )