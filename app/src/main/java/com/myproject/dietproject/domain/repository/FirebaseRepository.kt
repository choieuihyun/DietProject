package com.myproject.dietproject.domain.repository

import com.myproject.dietproject.data.db.remote.response.kcalresponse.User

interface FirebaseRepository {

    suspend fun test(value: String)

    suspend fun addUser(userId: String, user: User)

}