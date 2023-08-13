package com.myproject.dietproject.presentation.ui.userkcal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.dietproject.domain.usecase.AddUserTodayKcalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.floor

@HiltViewModel
class KcalDetailViewModel @Inject constructor(
    private val addUserTodayKcalUseCase : AddUserTodayKcalUseCase
) : ViewModel() {

    private var _kcalDetailDateText: MutableLiveData<String> = MutableLiveData()
    val kcalDetailDateText: LiveData<String>
        get() = _kcalDetailDateText

    private var _kcalDetailDataByDate: MutableLiveData<String> = MutableLiveData()
    val kcalDetailDataByDate: LiveData<String>
        get() = _kcalDetailDataByDate


    private val calendar = Calendar.getInstance() // 같은 Calendar 객체를 사용하지만, 각자의 dateFormat은 따로 해야한다.

    fun plusServingCalculator(serving: Float) : Float {

        var result = 0.0F

        result = serving + 0.5F

        return result

    }

    fun minusServingCalculator(serving: Float) : Float {

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

    fun getDate() {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dataByDate = dateFormat.format(calendar.time)
        var dateText = dateFormat.format(calendar.time).substring(5,10)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        when(dayOfWeek) {

            Calendar.MONDAY -> dateText = "$dateText (월)"

            Calendar.TUESDAY -> dateText = "$dateText (화)"

            Calendar.WEDNESDAY -> dateText = "$dateText (수)"

            Calendar.THURSDAY -> dateText = "$dateText (목)"

            Calendar.FRIDAY -> dateText = "$dateText (금)"

            Calendar.SATURDAY -> dateText = "$dateText(토)"

            Calendar.SUNDAY -> dateText = "$dateText(일)"

        }

        viewModelScope.launch {
            _kcalDetailDateText.postValue(dateText)
            _kcalDetailDataByDate.postValue(dataByDate)
        }

    }

    fun movePreviousDate() {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val previousDataByDate = dateFormat.format(calendar.time)
        var previousDateText = dateFormat.format(calendar.time).substring(5,10)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        when(dayOfWeek) {

            Calendar.MONDAY -> previousDateText = "$previousDateText (월)"

            Calendar.TUESDAY -> previousDateText = "$previousDateText (화)"

            Calendar.WEDNESDAY -> previousDateText = "$previousDateText (수)"

            Calendar.THURSDAY -> previousDateText = "$previousDateText (목)"

            Calendar.FRIDAY -> previousDateText = "$previousDateText (금)"

            Calendar.SATURDAY -> previousDateText = "$previousDateText (토)"

            Calendar.SUNDAY -> previousDateText = "$previousDateText (일)"

        }

        viewModelScope.launch {
            _kcalDetailDateText.postValue(previousDateText)
            _kcalDetailDataByDate.postValue(previousDataByDate)
        }

    }

    fun moveNextDate() {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val nextDataByDate = dateFormat.format(calendar.time)
        var nextDate = dateFormat.format(calendar.time).substring(5,10)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        when(dayOfWeek) {

            Calendar.MONDAY -> nextDate = "$nextDate (월)"

            Calendar.TUESDAY -> nextDate = "$nextDate (화)"

            Calendar.WEDNESDAY -> nextDate = "$nextDate (수)"

            Calendar.THURSDAY -> nextDate = "$nextDate (목)"

            Calendar.FRIDAY -> nextDate = "$nextDate (금)"

            Calendar.SATURDAY -> nextDate = "$nextDate (토)"

            Calendar.SUNDAY -> nextDate = "$nextDate (일)"

        }

        viewModelScope.launch {
            _kcalDetailDateText.postValue(nextDate)
            _kcalDetailDataByDate.postValue(nextDataByDate)
        }

    }


}