package com.myproject.dietproject.data.datasource.remotedatasource

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Date

import javax.inject.Inject

class FirebaseDataSource @Inject constructor(
    private val firebase: FirebaseDatabase
) {

    fun dbTest(): DatabaseReference {
        return firebase.getReference("test")
    }

    fun addUser(userId: String, userEmail: String): Task<Void> {
        return firebase.getReference("user").child(userId).let {
            it.child("email").setValue(userEmail)
            it.child("uid").setValue(userId)
        }
    }

    // 이걸 왜하냐면 로그인(구글,이메일)에서 personType이 비어있으면 personalInfo로 넘어가게 하려고 해놨음.
    fun getUser(): DatabaseReference { // 여기서 userId는 구글기준으론 account.id, 이메일 기준으론 uid
        return firebase.getReference("user")
    }

    fun getUserActivity(userId: String): DatabaseReference {
        return firebase.getReference("user").child(userId).child("activity")
    }


    fun addUserInfo(
        userId: String,
        gender: String,
        age: Int,
        height: Float,
        weight: Float,
        recommendKcal: Int,
        activity: String
    ) {

        firebase.getReference("user").child(userId).let {

            it.child("gender").setValue(gender)
            it.child("age").setValue(age)
            it.child("height").setValue(height)
            it.child("weight").setValue(weight)
            it.child("recommendKcal").setValue(recommendKcal)
            it.child("activity").setValue(activity)

        }

    }

    fun addTodayKcal(userId: String, kcal: Float, foodName: String, makerName: String, date: String) {
        firebase.getReference("user").child(userId).child("todayKcal").child(date).let {
            it.child("kcal").setValue(kcal)
            it.child("makerName").setValue(makerName)
            it.child("foodName").setValue(foodName)
        }
    }
}