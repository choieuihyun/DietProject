package com.myproject.dietproject.presentation.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.dietproject.domain.model.KcalDataForCalendar
import com.myproject.dietproject.domain.usecase.GetUserCalendarDetailUseCase
import com.myproject.dietproject.domain.usecase.GetUserRecommendKcalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarDetailViewModel @Inject constructor(
    private val getUserRecommendKcalUseCase: GetUserRecommendKcalUseCase,
    private val getUserCalendarDetailUseCase: GetUserCalendarDetailUseCase
) : ViewModel() {

    private val _dayKcal: MutableLiveData<Int> = MutableLiveData()
    val dayKcal: LiveData<Int> = _dayKcal

    private val _dayKcalList: MutableLiveData<ArrayList<KcalDataForCalendar?>> = MutableLiveData()
    val dayKcalList: LiveData<ArrayList<KcalDataForCalendar?>> = _dayKcalList

    private var _recommendKcal: MutableLiveData<Int> = MutableLiveData()
    val recommendKcal: LiveData<Int> = _recommendKcal

    private var _imageResultLiveData: MutableLiveData<Int> = MutableLiveData()
    val imageResultLiveData: LiveData<Int> = _imageResultLiveData

    private var _calendarDetailAlert: MutableLiveData<String> = MutableLiveData()
    val calendarDetailAlert: LiveData<String> = _calendarDetailAlert

    fun getCalendarDetailData(userId: String, date: String) {

        viewModelScope.launch {

            getUserCalendarDetailUseCase(userId, date)

            _dayKcal.value = getUserCalendarDetailUseCase.getDayKcal()

            _dayKcalList.value = getUserCalendarDetailUseCase.getDayKcalList()

        }

    }

    fun getRecommendKcalData(userId: String) {

        viewModelScope.launch {

            getUserRecommendKcalUseCase(userId)

            _recommendKcal.value = getUserRecommendKcalUseCase.getRecommendKcal().value

            imageSetting()

        }

    }

    private fun imageSetting() {

        if ((dayKcal.value ?: 0) >= (recommendKcal.value ?: 0)) {

            _imageResultLiveData.value = 1
            _calendarDetailAlert.value = "운동해야해요 !"

        } else {

            _imageResultLiveData.value = 2
            _calendarDetailAlert.value = "잘했어요 !"

        }

    }
}