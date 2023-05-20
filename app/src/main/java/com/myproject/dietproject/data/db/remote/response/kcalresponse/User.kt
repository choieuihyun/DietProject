package com.myproject.dietproject.data.db.remote.response.kcalresponse

import androidx.room.PrimaryKey
import java.util.Date

data class User (

    @PrimaryKey val uid: String, // firebase uid
    @PrimaryKey val email: String, // 이메일
    val personType: PersonType?, // 활동량
    val todayKcal: TodayKcal? // 오늘 먹은 칼로리

        )

data class PersonType ( // 활동량 타입

    val height: Float, // 키
    val weight: Float // 몸무게

        )

data class TodayKcal ( // 하루에 먹은 칼로리

    val kcal: Float,
    val foodName: String,
    val date: Date

        )