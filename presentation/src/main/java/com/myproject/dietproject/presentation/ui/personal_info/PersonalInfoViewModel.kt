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



    fun addUserInfo(
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

    }



}