package com.myproject.dietproject.data.mapper

import com.google.android.gms.tasks.Task
import com.myproject.dietproject.data.db.remote.firebase.userdata.User
import com.myproject.dietproject.domain.model.UserModel

fun User.toModel() = UserModel(

    uid = uid,
    email = email,
    gender = gender,
    age = age,
    height = height,
    weight= weight,
    activity = activity,
    todayKcal = null

)

fun UserModel.toEntity() = User(
    uid = uid,
    email = email,
    age = age,
    height = height,
    weight = weight,
    activity = activity,
    todayKcal = null
)

/*
fun <T> Task<T>.map(user: User) : UserModel {
    return UserModel(user.uid,
        user.email,
        user.age,
        user.height,
        user.weight,
        user.activity,
        user.todayKcal)
}
*/


