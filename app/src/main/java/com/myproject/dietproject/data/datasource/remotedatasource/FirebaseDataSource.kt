package com.myproject.dietproject.data.datasource.remotedatasource

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.myproject.dietproject.data.db.remote.firebase.userdata.User

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


    // 이걸 왜하냐면 로그인(구글,이메일)에서 personType이 비어있으면 personalInfo로 넘어가게 하려고 해놨음.
    fun getUser(userId: String) : DatabaseReference { // 여기서 userId는 구글기준으론 account.id, 이메일 기준으론 uid
        return firebase.getReference("user").child(userId)
    }


}