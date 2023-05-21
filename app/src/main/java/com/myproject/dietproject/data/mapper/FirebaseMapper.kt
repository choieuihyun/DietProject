package com.myproject.dietproject.data.mapper

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.myproject.dietproject.data.db.remote.firebase.userdata.User
import com.myproject.dietproject.domain.model.UserModel

fun User.toModel() = UserModel(

    uid = uid,
    email = email,
    age = age,
    height = height,
    weight= weight,
    activity = activity,
    todayKcal = todayKcal

)

fun UserModel.toEntity() = User(
    uid = uid,
    email = email,
    age = age,
    height = height,
    weight= weight,
    activity = activity,
    todayKcal = todayKcal
)

fun <T> Task<T>.map(user: User) : UserModel {
    return UserModel(user.uid,
        user.email,
        user.age,
        user.height,
        user.weight,
        user.activity,
        user.todayKcal)
}


