package com.myproject.dietproject.domain.usecase

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.myproject.dietproject.data.db.remote.firebase.userdata.User
import com.myproject.dietproject.domain.repository.FirebaseRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {

    suspend operator fun invoke(userId: String) : DatabaseReference { // 아 생각을 잘못했다. 이게 email이랑 google 로그인 둘 다 쓰는건데.
        Log.d("getUserByUseCase",repository.getUser(userId).toString())
        return repository.getUser(userId)
    }

}