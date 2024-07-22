package com.myproject.dietproject.presentation.ui.userkcal

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.dietproject.domain.model.FoodDiaryModel
import com.myproject.dietproject.domain.model.Kcal
import com.myproject.dietproject.domain.usecase.AddUserTodayKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserNextDateKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserPreviousDateKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserTodayKcalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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

    private var _kcalData = MutableLiveData<Kcal?>()
    val kcalData: LiveData<Kcal?> = _kcalData

    private var _foodDiaryData = MutableLiveData<FoodDiaryModel?>()
    val foodDiaryData: LiveData<FoodDiaryModel?> = _foodDiaryData

    fun setKcalData(kcal: Kcal?) {
        _kcalData.value = kcal
    }

    fun setFoodDiaryData(foodDiaryModel: FoodDiaryModel?) {
        _foodDiaryData.value = foodDiaryModel
    }


    /**
     * 먹는 양(인분) 계산기(+)
     */
    fun plusServingCalculator(serving: Float) : Float { // 인분 +

        var result = 0.0F

        result = serving + 0.5F

        return result

    }

    /**
     * 먹는 양(인분) 계산기(-)
     */
    fun minusServingCalculator(serving: Float) : Float { // 인분 -

        var result = serving - 0.5F

        if(result <= 0)
            result = serving - 0.0F

        return result

    }

    /**
     * 먹는 양 칼로리 계산기(+)
     */
    fun plusCalculator(data: Float, number: Float): Float {

        return data * number

    }

    /**
     * 먹는 양 칼로리 계산기(-)
     */
    fun minusCalculator(data: Float, data2: Float): Float {

        val result = data - (data2 * 0.5F)

        if(result <= 0)
            return data

        return result

    }

    /**
     * DB에 먹은 음식 추가하는 메서드.
     * 시간 파싱하는 구조는 "2024-06-05" 와 같은 "연-월-일" 문자열에 앱을 사용하는 현재 "시-분-초" 추가
     * @param 유저 아이디, 칼로리, 음식 이름, 제조사 명, 칼로리 등록 날짜
     */
    fun addUserTodayKcal(userId: String, kcal: Float, foodName: String, makerName: String, dateText: String) {

        val currentTime = LocalTime.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = LocalDate.parse(dateText, dateFormatter)

        val dateTime = LocalDateTime.of(date, currentTime)
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = dateTime.format(dateTimeFormatter)

        viewModelScope.launch {
            addUserTodayKcalUseCase(userId, floor(kcal), foodName, makerName, formattedDateTime.toString())
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

    /**
     * 전 날짜로 이동하는 메서드
     */
    fun movePreviousDate(userId: String) {

        viewModelScope.launch {

            getPreviousDateText(userId)

            _dayDateText.value = getPreviousDateText.getPreviousDateText()
            _dayByDateText.value = getPreviousDateText.getPreviousDateTextByDate()

            _kcalDetailDateText.postValue(_dayDateText.value)
            _kcalDetailDataByDate.postValue(_dayByDateText.value)

        }
    }

    /**
     * 다음 날짜로 이동하는 메서드
     */
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