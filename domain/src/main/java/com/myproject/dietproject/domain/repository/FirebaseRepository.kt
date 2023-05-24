package com.myproject.dietproject.domain.repository

import com.google.firebase.database.DatabaseReference
import com.myproject.dietproject.domain.model.UserModel

interface FirebaseRepository {

    suspend fun test(value: String)

    suspend fun addUser(userId: String, user: UserModel)

    suspend fun getUser(userId: String) : DatabaseReference

    suspend fun addUserInfo(userId: String,
                            gender: String,
                            age: Int,
                            height: Float,
                            weight: Float,
                            activity: String)

}