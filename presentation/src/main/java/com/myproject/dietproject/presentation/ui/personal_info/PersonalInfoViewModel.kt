package com.myproject.dietproject.presentation.ui.personal_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.dietproject.domain.usecase.AddUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.floor

@HiltViewModel
class PersonalInfoViewModel @Inject constructor(
    private val addUserInfoUseCase: AddUserInfoUseCase
) : ViewModel(){

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
                _userGender,
                _userAge,
                roundToHeight,
                roundToWeight,
                setRecommendKcal(),
                _userActivity
            )
        }
    }
}