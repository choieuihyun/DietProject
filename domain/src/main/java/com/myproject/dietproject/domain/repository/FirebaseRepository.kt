package com.myproject.dietproject.domain.repository

import android.provider.ContactsContract.Data
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.myproject.dietproject.domain.model.UserModel
import java.util.Date

interface FirebaseRepository {

    suspend fun test(value: String)

    suspend fun addUser(userId: String, userEmail: String)

    suspend fun getUser(userId: String) : DatabaseReference

    suspend fun getUserTodayKcal(userId: String, date: String) : DatabaseReference

    suspend fun getUserWeekKcal(userId: String) : DatabaseReference

    suspend fun getUserRecommendKcal(userId: String) : DatabaseReference

    suspend fun getUserActivity(userId: String) : DatabaseReference

    suspend fun addUserInfo(userId: String,
                            gender: String,
                            age: Int,
                            height: Float,
                            weight: Float,
                            recommendKcal: Int,
                            activity: String)

    suspend fun addTodayKcal(userId: String, kcal: Float, foodName: String, makerName: String, date: String)

}