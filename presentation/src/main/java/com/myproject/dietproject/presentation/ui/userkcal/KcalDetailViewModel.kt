package com.myproject.dietproject.presentation.ui.userkcal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.dietproject.domain.usecase.AddUserTodayKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserNextDateKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserPreviousDateKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserTodayKcalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.floor

@HiltViewModel
class KcalDetailViewModel @Inject constructor(
    private val addUserTodayKcalUseCase : AddUserTodayKcalUseCase,
    private val getTodayDateText: GetUserTodayKcalUseCase,
    private val getPreviousDateText: GetUserPreviousDateKcalUseCase,
    private val getNextDateText: GetUserNextDateKcalUseCase
) : ViewModel() {

    private var _kcalDetailDateText: MutableLiveData<String> = MutableLiveData() // detail 화면 상단의 06-28 (금)과 같은 방식으로 표시하기 위한 변수
    val kcalDetailDateText: LiveData<String>
        get() = _kcalDetailDateText

    private var _kcalDetailDataByDate: MutableLiveData<String> = MutableLiveData() // detail 화면 상단의 06-28 (금)과 같은 방식으로 표시하기 위한 변수
    val kcalDetailDataByDate: LiveData<String>
        get() = _kcalDetailDataByDate

    private var _dayDateText: MutableLiveData<String> = MutableLiveData()
    val dayDateText: LiveData<String>
        get() = _dayDateText

    private var _dayByDateText: MutableLiveData<String> = MutableLiveData() // 데이터를 추가할 때 2023-06-28와 같은 방식으로 추가하기 위한 변수
    val dateByDateText: LiveData<String>
        get() = _dayByDateText


    fun plusServingCalculator(serving: Float) : Float { // 인분 +

        var result = 0.0F

        result = serving + 0.5F

        return result

    }

    fun minusServingCalculator(serving: Float) : Float { // 인분 -

        var result = serving - 0.5F

        if(result <= 0)
            result = serving - 0.0F

        return result

    }

    fun plusCalculator(data: Float, number: Float): Float {

        return data * number

    }

    fun minusCalculator(data: Float, data2: Float): Float {

        val result = data - (data2 * 0.5F)

        if(result <= 0)
            return data

        return result

    }

    fun addUserTodayKcal(userId: String, kcal: Float, foodName: String, makerName: String, date: String) {

        viewModelScope.launch {
            addUserTodayKcalUseCase(userId, floor(kcal), foodName, makerName, date)
        }

    }

    fun getDate(userId: String) {

        viewModelScope.launch {

            getTodayDateText(userId)

            _dayDateText.value = getTodayDateText.getHomeDateText()
            _dayByDateText.value = getTodayDateText.getDateTextByDate()

            _kcalDetailDateText.postValue(_dayDateText.value)
            _kcalDetailDataByDate.postValue(_dayByDateText.value)

        }

    }

    fun movePreviousDate(userId: String) {

        viewModelScope.launch {

            getPreviousDateText(userId)

            _dayDateText.value = getPreviousDateText.getPreviousDateText()
            _dayByDateText.value = getPreviousDateText.getPreviousDateTextByDate()

            _kcalDetailDateText.postValue(_dayDateText.value)
            _kcalDetailDataByDate.postValue(_dayByDateText.value)

        }
    }
    

    fun moveNextDate(userId: String) {

        viewModelScope.launch {

            getNextDateText(userId)

            _dayDateText.value = getNextDateText.getNextDateText()
            _dayByDateText.value = getNextDateText.getNextDateTextByDate()

            _kcalDetailDateText.postValue(_dayDateText.value)
            _kcalDetailDataByDate.postValue(_dayByDateText.value)

        }

    }


}