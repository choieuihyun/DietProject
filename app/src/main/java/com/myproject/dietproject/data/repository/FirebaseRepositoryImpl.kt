package com.myproject.dietproject.data.repository

import com.myproject.dietproject.data.datasource.remotedatasource.FirebaseDataSource
import com.myproject.dietproject.data.db.remote.response.kcalresponse.User
import com.myproject.dietproject.domain.repository.FirebaseRepository
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : FirebaseRepository {

    override suspend fun test(value: String) {
        firebaseDataSource.dbTest().setValue(value)
    }

    override suspend fun addUser(userId: String, user: User) {
        firebaseDataSource.addUser(userId, user)
    }

}