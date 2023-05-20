package com.myproject.dietproject.data.datasource.remotedatasource

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.myproject.dietproject.data.db.remote.response.kcalresponse.User
import javax.inject.Inject

class FirebaseDataSource @Inject constructor(
    private val firebase: FirebaseDatabase
) {

    fun dbTest() : DatabaseReference {
        return firebase.getReference("test")
    }

    fun addUser(userId: String, user: User): Task<Void> {
        return firebase.getReference("user").child(userId).setValue(user)

    }


}