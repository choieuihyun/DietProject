package com.myproject.dietproject.data.repository

import com.google.firebase.database.DatabaseReference
import com.myproject.dietproject.data.datasource.remotedatasource.FirebaseDataSource
import com.myproject.dietproject.data.mapper.toEntity
import com.myproject.dietproject.domain.model.UserModel
import com.myproject.dietproject.domain.repository.FirebaseRepository
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : FirebaseRepository {

    override suspend fun test(value: String) {
        firebaseDataSource.dbTest().setValue(value)
    }

    override suspend fun addUser(userId: String, user: UserModel) {
        firebaseDataSource.addUser(userId, user.toEntity())
    }

    override suspend fun getUser(userId: String) : DatabaseReference {
        return firebaseDataSource.getUser(userId)
    }

    override suspend fun addUserInfo(
        userId: String,
        gender: String,
        age: Int,
        height: Float,
        weight: Float,
        activity: String
    ) {
        firebaseDataSource.addUserInfo(userId, gender, age, height, weight, activity)
    }


}