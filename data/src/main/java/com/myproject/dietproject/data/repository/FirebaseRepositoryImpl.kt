package com.myproject.dietproject.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.myproject.dietproject.data.datasource.remotedatasource.FirebaseDataSource
import com.myproject.dietproject.data.mapper.toEntity
import com.myproject.dietproject.domain.model.UserModel
import com.myproject.dietproject.domain.repository.FirebaseRepository
import java.util.Date
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : FirebaseRepository {

    override suspend fun test(value: String) {
        firebaseDataSource.dbTest().setValue(value)
    }

    override suspend fun addUser(userId: String, userEmail: String) {
        firebaseDataSource.addUser(userId, userEmail)
    }

    override suspend fun getUser(userId: String) : DatabaseReference {
        return firebaseDataSource.getUser().child(userId)
    }

    override suspend fun getUserTodayKcal(userId: String, date: String): DatabaseReference {
        return firebaseDataSource.getUser().child(userId).child("todayKcal")
    }

    override suspend fun getUserRecommendKcal(userId: String) : DatabaseReference {
        return firebaseDataSource.getUser().child(userId).child("recommendKcal")
    }


    override suspend fun getUserActivity(userId: String): DatabaseReference {
        return firebaseDataSource.getUserActivity(userId)
    }

    override suspend fun addUserInfo(
        userId: String,
        gender: String,
        age: Int,
        height: Float,
        weight: Float,
        recommendKcal: Int,
        activity: String
    ) {
        firebaseDataSource.addUserInfo(userId, gender, age, height, weight, recommendKcal, activity)
    }

    override suspend fun addTodayKcal(userId: String, kcal: Float, foodName: String, makerName: String, date: String) {
        firebaseDataSource.addTodayKcal(userId, kcal, foodName, makerName, date)
    }


}