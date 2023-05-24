package com.myproject.dietproject.data.datasource.remotedatasource

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.myproject.dietproject.data.db.remote.firebase.userdata.User

import javax.inject.Inject

class FirebaseDataSource @Inject constructor(
    private val firebase: FirebaseDatabase
) {

    fun dbTest(): DatabaseReference {
        return firebase.getReference("test")
    }

    fun addUser(userId: String, user: User): Task<Void> {
        return firebase.getReference("user").child(userId).setValue(user)
    }


    // 이걸 왜하냐면 로그인(구글,이메일)에서 personType이 비어있으면 personalInfo로 넘어가게 하려고 해놨음.
    fun getUser(userId: String): DatabaseReference { // 여기서 userId는 구글기준으론 account.id, 이메일 기준으론 uid
        return firebase.getReference("user").child(userId)
    }

//    fun addUserInfo(userId: String, data: Any) { 이렇게 타입 검사로 분기해서 하고싶은데
//
//    }

    fun addUserGenderInfo(userId: String, gender: String) {
        firebase.getReference("user").child(userId).child(gender).setValue(gender)
    }

    fun addUserAgeInfo(userId: String, age: Int) {
        firebase.getReference("user").child(userId).child("age").setValue(age)
    }

    fun addUserHeightInfo(userId: String, height: Float) {
        firebase.getReference("user").child(userId).child("height").setValue(height)
    }

    fun addUserWeightInfo(userId: String, weight: Float) {
        firebase.getReference("user").child(userId).child("weight").setValue(weight)
    }

    fun addUserActivityInfo(userId: String, activity: String) {
        firebase.getReference("user").child(userId).child(activity).setValue(activity)
    }

    fun addUserInfo(
        userId: String,
        gender: String,
        age: Int,
        height: Float,
        weight: Float,
        activity: String
    ) {

        firebase.getReference("user").child(userId).let {

            it.child("gender").setValue(gender)
            it.child("age").setValue(age)
            it.child("height").setValue(height)
            it.child("weight").setValue(weight)
            it.child("activity").setValue(activity)

        }

    }
}