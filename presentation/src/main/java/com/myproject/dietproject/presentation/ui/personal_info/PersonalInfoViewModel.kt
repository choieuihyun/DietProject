package com.myproject.dietproject.presentation.ui.personal_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.myproject.dietproject.domain.usecase.AddUserInfoUseCase
import com.myproject.dietproject.domain.usecase.AddUserUseCase
import com.myproject.dietproject.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.floor

@HiltViewModel
class PersonalInfoViewModel @Inject constructor(
    private val addUserInfoUseCase: AddUserInfoUseCase,
    private val addUserUseCase: AddUserUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel(){

    private var _userName: String = ""
    val userName: String
        get() = _userName

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

    private var _userRecommendKcal: Float = 0.0F
    val userRecommendKcal: Float
        get() = _userRecommendKcal

    private var _userActivity: String = ""
    val userActivity: String
        get() = _userActivity

    private var _userTargetWeight: Float = 0.0F
    val userTargetWeight: Float
        get() = _userTargetWeight


    fun setNameInfo(name: String) {
        _userName = name
    }

    fun setGenderInfo(gender: String) {
        _userGender = gender
    }

    fun setAgeInfo(age: Int) {
        _userAge = age
    }

    fun setHeightInfo(height: Float) {
        _userHeight = height
    }

    fun setWeightInfo(weight: Float) {
        _userWeight = weight
    }

    fun setTargetWeightInfo(targetWeight: Float) {
        _userTargetWeight = targetWeight
    }

    fun setActivityInfo(activity: String) {
        _userActivity = activity
    }

    private fun setRecommendKcal() : Int { // 다이어트시 하루 권장 섭취 칼로리 공식

        var avgWeight = 0.0F

        if(_userGender == "male") {

            avgWeight = (_userHeight/100) * (_userHeight/100) * 22

            when (_userActivity) {
                "lightActivity" -> _userRecommendKcal = (avgWeight * 27) - 500
                "middleActivity" -> _userRecommendKcal = (avgWeight * 32) - 500
                "hardActivity" -> _userRecommendKcal = (avgWeight * 37) - 500
            }


        } else if(_userGender == "female") {

            avgWeight = (_userHeight/100) * (_userHeight/100) * 21

            when (_userActivity) {
                "lightActivity" -> _userRecommendKcal = (avgWeight * 27) - 500
                "middleActivity" -> _userRecommendKcal = (avgWeight * 32) - 500
                "hardActivity" -> _userRecommendKcal = (avgWeight * 37) - 500
            }

        }

        return _userRecommendKcal.toInt()
    }


    fun addUserInfo(
        userId: String
    ) {

        viewModelScope.launch {

            val roundToHeight = floor(_userHeight) // 소수점 버리기
            val roundToWeight = floor(_userWeight)

            addUserInfoUseCase.invoke(
                userId,
                _userName,
                _userGender,
                _userAge,
                roundToHeight,
                roundToWeight,
                _userTargetWeight,
                setRecommendKcal(),
                _userActivity
            )
        }
    }

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

                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }

    }


}