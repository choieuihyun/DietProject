package com.myproject.dietproject.presentation.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.myproject.dietproject.domain.model.UserModel
import com.myproject.dietproject.domain.usecase.AddUserInfoUseCase
import com.myproject.dietproject.domain.usecase.AddUserUseCase
import com.myproject.dietproject.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.floor
import kotlin.math.roundToInt


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val addUserUseCase: AddUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val addUserInfoUseCase: AddUserInfoUseCase
): ViewModel(){

    fun addUser(userId: String, user: UserModel) {

        viewModelScope.launch {

            addUserUseCase(userId, user)

        }
    }

    fun getUser(userId: String){

        viewModelScope.launch {

            getUserUseCase(userId).addValueEventListener(object : ValueEventListener { // 데이터 변화를 감지하는 리스너이기 때문에
                                                                                       // 회원가입에만 반응해야 하는데 왜 구글 로그인에는 다 반응함?
                override fun onDataChange(snapshot: DataSnapshot) {

                    Log.d("getUserByViewModel", snapshot.getValue(UserModel::class.java).toString())


                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }

    }

    fun addUserInfo(userId: String,
                   gender: String,
                   age: Int,
                   height: Float,
                   weight: Float,
                   activity: String) {

        viewModelScope.launch {

            val roundToHeight = floor(height) // 소수점 버리기
            val roundToWeight = floor(weight)

            addUserInfoUseCase.invoke(userId, gender, age, roundToHeight, roundToWeight, activity)

        }

    }


}