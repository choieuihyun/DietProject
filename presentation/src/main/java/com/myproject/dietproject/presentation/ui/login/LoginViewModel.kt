package com.myproject.dietproject.presentation.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.myproject.dietproject.domain.model.UserModel
import com.myproject.dietproject.domain.usecase.AddUserInfoUseCase
import com.myproject.dietproject.domain.usecase.AddUserUseCase
import com.myproject.dietproject.domain.usecase.GetUserActivityUseCase
import com.myproject.dietproject.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.floor


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val addUserUseCase: AddUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getUserActivityUseCase: GetUserActivityUseCase,
    private val addUserInfoUseCase: AddUserInfoUseCase
): ViewModel() {

    private var _userGender: String = ""
    val userGender: String
        get() = _userGender

    private var _userAge: Int = 0
    val userAge: Int
        get() = _userAge

    private var _userHeight: Float = 0.0F
    val userHeight: Float
        get() = _userHeight

    private var _userWeight: Float = 0.0F
    val userWeight: Float
        get() = _userWeight

    private var _userActivity: String = ""
    val userActivity: String
        get() = _userActivity


    private var _loginUserActivity: Any? = "" // LoginFragment -> Home, PersonalInfo 분기점 기준 변수
    val loginUserActivity: Any?
        get() = _loginUserActivity

    fun addUser(userId: String, userEmail: String) {

        viewModelScope.launch {

            addUserUseCase(userId, userEmail)

        }
    }

    fun getUser(userId: String) {

        viewModelScope.launch {

            getUserUseCase(userId).addValueEventListener(object :
                ValueEventListener { // 데이터 변화를 감지하는 리스너이기 때문에
                // 회원가입에만 반응해야 하는데 왜 구글 로그인에는 다 반응함?
                override fun onDataChange(snapshot: DataSnapshot) {

                    Log.d("getUserByViewModel", snapshot.getValue(UserModel::class.java).toString())

                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }

    }

    fun getUserActivity(userId: String) {

        viewModelScope.launch {

            getUserActivityUseCase(userId).get().addOnSuccessListener {

                Log.i("viewModel_4", it.value.toString())
                _loginUserActivity = it.value
                Log.i("viewModel_5", _loginUserActivity.toString())

            }.addOnFailureListener {
                Log.i("sdfsdfsdfFailed", it.message.toString())
            }

        }
    }

//        fun setGenderInfo(gender: String) {
//            _userGender = gender
//        }
//
//        fun setAgeInfo(age: Int) {
//            _userAge = age
//        }
//
//        fun setHeightInfo(height: Float) {
//            _userHeight = height
//        }
//
//        fun setWeightInfo(weight: Float) {
//            _userWeight = weight
//        }
//
//        fun setActivityInfo(activity: String) {
//            _userActivity = activity
//        }

/*        fun addUserInfo(
            userId: String,
            gender: String,
            age: Int,
            height: Float,
            weight: Float,
            activity: String
        ) {

            viewModelScope.launch {

                val roundToHeight = floor(_userHeight) // 소수점 버리기
                val roundToWeight = floor(_userWeight)

                addUserInfoUseCase.invoke(
                    userId,
                    _userGender,
                    _userAge,
                    roundToHeight,
                    roundToWeight,
                    _userActivity
                )

            }

        }*/
    }

