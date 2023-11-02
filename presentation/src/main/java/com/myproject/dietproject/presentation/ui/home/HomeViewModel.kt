package com.myproject.dietproject.presentation.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.dietproject.domain.model.Kcal
import com.myproject.dietproject.domain.usecase.GetUserNextDateKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserPreviousDateKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserRecommendKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserTodayKcalUseCase
import com.myproject.dietproject.presentation.ui.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserTodayKcalUseCase: GetUserTodayKcalUseCase,
    private val getUserPreviousDateKcalUseCase: GetUserPreviousDateKcalUseCase,
    private val getUserNextDateKcalUseCase: GetUserNextDateKcalUseCase,
    private val getUserRecommendKcalUseCase: GetUserRecommendKcalUseCase,

) : ViewModel() {

    private val _kcalData = MutableLiveData<List<Kcal>?>()
    val kcalData: LiveData<List<Kcal>?> = _kcalData

    // 1회 섭취 총 칼로리 평균내서 음식 누르면 추가하게 하려고 했는뎅..
    private var _kcalNUTRCONT = MutableLiveData<Float?>()
    val kcalNUTRCONT: LiveData<Float?> = _kcalNUTRCONT

    private val _showToast: MutableLiveData<Event<String>> = MutableLiveData()
    val showToast: LiveData<Event<String>> = _showToast

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError: MutableLiveData<Boolean> = MutableLiveData()
    val isError: LiveData<Boolean> = _isError

    private var _todayKcal: MutableLiveData<Int?> = MutableLiveData() // 오늘 섭취한 총 칼로리
    val todayKcal: LiveData<Int?> = _todayKcal

    private var _recommendKcal: MutableLiveData<Int> = MutableLiveData() // 일일 권장 칼로리
    val recommendKcal: LiveData<Int> = _recommendKcal

    private var _scarceKcal: MutableLiveData<Int> = MutableLiveData() // 부족한 섭취량 (Kcal)
    val scarceKcal: LiveData<Int> = _scarceKcal

    private var _homeDateText: MutableLiveData<String?> = MutableLiveData() // 날짜 text
    val homeDateText: LiveData<String?>
        get() = _homeDateText

    private var _homeDataByDate: MutableLiveData<String> = MutableLiveData() // 날짜 기준 데이터
    val homeDataByDate: LiveData<String>
        get() = _homeDataByDate

    private var _imageResultLiveData: MutableLiveData<Int> = MutableLiveData()
    val imageResultLiveData: LiveData<Int>
        get() = _imageResultLiveData

    private var _kcalAlert: MutableLiveData<String> = MutableLiveData()
    val kcalAlert: LiveData<String>
        get() = _kcalAlert

    private var calculTodayKcal = 0 // 연산용도 오늘 섭취한 칼로리
    private var calculRecommendKcal = 0 // 연산용도 권장 칼로리


    fun getUserTodayKcalData(userId: String) {

        viewModelScope.launch {

            getUserTodayKcalUseCase(userId)

           _todayKcal.value = getUserTodayKcalUseCase.getTodayKcal()

            _homeDateText.value = getUserTodayKcalUseCase.getHomeDateText()

            calculTodayKcal = getUserTodayKcalUseCase.getCalCulTodayKcal()

            imageSetting(_recommendKcal.value ?: 0, _todayKcal.value ?: 0)

        }

    }

    fun getUserRecommendKcalData(userId: String) {

        viewModelScope.launch {

            getUserRecommendKcalUseCase(userId)

            _recommendKcal.value = getUserRecommendKcalUseCase.getRecommendKcal()
            calculRecommendKcal = getUserRecommendKcalUseCase.getCalCulRecommendKcal()
            //_scarceKcal.value = getUserRecommendKcalUseCase.getScarceKcal()

        }

    }

    fun getUserScarceKcalData(todayKcal: Int) {

        _scarceKcal.value = recommendKcal.value?.minus(todayKcal)

    }


    fun movePreviousDate(userId: String) {

        viewModelScope.launch {

            getUserPreviousDateKcalUseCase(userId)

            _todayKcal.value = getUserPreviousDateKcalUseCase.getPreviousDateKcal()

            _homeDateText.value = getUserPreviousDateKcalUseCase.getHomePreviousDateText()

            calculTodayKcal = getUserPreviousDateKcalUseCase.getCalCulPreviousDateKcal()

            imageSetting(recommendKcal.value ?: 0, todayKcal.value ?: 0)

        }

    }

    fun moveNextDate(userId: String) {

        viewModelScope.launch {

            getUserNextDateKcalUseCase(userId)

            _todayKcal.value = getUserNextDateKcalUseCase.getNextDateKcal()

            _homeDateText.value = getUserNextDateKcalUseCase.getHomeNextDateText()

            calculTodayKcal = getUserNextDateKcalUseCase.getCalCulNextDateKcal()

            imageSetting(recommendKcal.value ?: 0, todayKcal.value ?: 0)

        }

    }

    private fun imageSetting(recommendKcal: Int, todayKcal: Int) { // 파라미터로 받아서 처리하는 것은 순서가 꼬이나?

        try {
            if (recommendKcal * 0.7 >= todayKcal) { // 권장 섭취 칼로리 * 0.7 > 오늘 섭취 칼로리

                _kcalAlert.value = ("더 먹어야 해요 !")
                _imageResultLiveData.value = 1// 배고픈 이미지_

            } else if (recommendKcal * 0.7 < todayKcal && todayKcal <= recommendKcal) {

                _kcalAlert.value = ("슬슬 배가 불러요 !")
                _imageResultLiveData.value = 2 // 배부른 이미지

            } else {

                _kcalAlert.value = ("그만 먹어라")
                _imageResultLiveData.value = 3 // 배부른 이미지는 같은데 초과

            }
        } catch (e: NullPointerException) {
            _kcalAlert.value = "null"
            _imageResultLiveData.value = 1// 배고픈 이미지

        }


    }




}

