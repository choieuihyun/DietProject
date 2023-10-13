package com.myproject.dietproject.presentation.ui.weightChart

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.utils.EntryXComparator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.myproject.dietproject.domain.model.TodayKcal
import com.myproject.dietproject.domain.usecase.GetUserTodayKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserWeekKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserWeightUseCase
import com.myproject.dietproject.presentation.ui.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.apache.commons.lang3.ObjectUtils.Null
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Collections
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WeightChartViewModel @Inject constructor(
    private val getUserWeekKcalUseCase: GetUserWeekKcalUseCase,
    private val getUserWeightUseCase: GetUserWeightUseCase
) : ViewModel() {

    // 이번 주 시작 날짜, 마지막 날짜

    private var _startOfWeek: MutableLiveData<String> = MutableLiveData()
    val startOfWeek: LiveData<String>
        get() = _startOfWeek

    private var _endOfWeek: MutableLiveData<String> = MutableLiveData()
    val endOfWeek: LiveData<String>
        get() = _endOfWeek

    // 전 주 시작 날짜, 마지막 날짜

    private var _startOfPreviousWeek: MutableLiveData<String> = MutableLiveData()
    val startOfPreviousWeek: LiveData<String> = _startOfWeek

    private var _endOfPreviousWeek: MutableLiveData<String> = MutableLiveData()
    val endOfPreviousWeek: LiveData<String> = _endOfWeek

    // 다음 주 시작 날짜, 마지막 날짜

    private var _startOfNextWeek: MutableLiveData<String> = MutableLiveData()
    val startOfNextWeek: LiveData<String> = _startOfWeek

    private var _endOfNextWeek: MutableLiveData<String> = MutableLiveData()
    val endOfNextWeek: LiveData<String> = _endOfWeek

    // 이번 주 칼로리 리스트

    private var _weekKcalArray: MutableLiveData<Event<MutableList<Int>>> = MutableLiveData()
    val weekKcalArray: LiveData<Event<MutableList<Int>>>
        get() = _weekKcalArray

    // 이번 주 날짜 별 칼로리 합

    private var _weekKcalSum: MutableLiveData<Int> = MutableLiveData()
    val weekKcalSum: LiveData<Int>
        get() = _weekKcalSum

    // 이번 주 날짜 리스트

    private var _weekDateArray: MutableLiveData<Event<MutableList<String>>> = MutableLiveData()
    val weekDateArray: LiveData<Event<MutableList<String>>>
        get() = _weekDateArray

    // 전 주 날짜 리스트

    private var _previousWeekDateArray: MutableLiveData<Event<MutableList<String>>> = MutableLiveData()
    val previousWeekDateArray: LiveData<Event<MutableList<String>>>
        get() = _previousWeekDateArray

    // 전 주 칼로리 리스트

    private var _previousWeekKcalArray: MutableLiveData<Event<MutableList<Int>>> = MutableLiveData()
    val previousWeekKcalArray: LiveData<Event<MutableList<Int>>>
        get() = _previousWeekKcalArray

    // 다음 주 날짜 리스트

    private var _nextWeekDateArray: MutableLiveData<Event<MutableList<String>>> = MutableLiveData()
    val nextWeekDateArray: LiveData<Event<MutableList<String>>>
        get() = _nextWeekDateArray

    // 다음 주 칼로리 리스트

    private var _nextWeekKcalArray: MutableLiveData<Event<MutableList<Int>>> = MutableLiveData()
    val nextWeekKcalArray: LiveData<Event<MutableList<Int>>>
        get() = _nextWeekKcalArray

    // 달리기 칼로리 소모량

    private var _howMuchRunning: MutableLiveData<Float> = MutableLiveData()
    val howMuchRunning: LiveData<Float>
        get() = _howMuchRunning

    // 유저의 몸무게

    private var _weight: MutableLiveData<Int> = MutableLiveData()
    val weight: LiveData<Int>
        get() = _weight

    private val _isNextButtonEnabled: MutableLiveData<Boolean> = MutableLiveData(true)
    val isNextButtonEnabled: LiveData<Boolean>
        get() = _isNextButtonEnabled

    private val _entries: MutableList<Entry> = mutableListOf()
    val entries: MutableList<Entry>
        get() = _entries

    fun getUserWeight(userId: String) {

        viewModelScope.launch {

            getUserWeightUseCase(userId).addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    _weight.value = snapshot.value.toString().toInt()

                    updateChartData() // 이렇게 비동기 순서 맞추는게 맞냐?
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }

    }


    fun getChartWeekData(userId: String) {

        val calendar = Calendar.getInstance()
        val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR) // 24(24번째 주) 이렇게 몇번째 주로 숫자로 나온다.

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY) // DAY_OF_WEEK의 구조로, MONDAY를 시작으로 calendar 세팅.
        val startOfWeekCalendar =
            calendar.time // Mon Jun 12 00:59:28 GMT+09:00 2023 시작을 월요일 ( 해당 주를 받아서 )

        calendar.add(Calendar.DAY_OF_WEEK, 6)
        val endOfWeekCalendar = calendar.time // Sun Jun 18 00:59:28 GMT+09:00 2023 해당 주의 마지막, 일요일

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val startOfWeekFormatting = dateFormat.format(startOfWeekCalendar) // 2023-06-12로 변환
        val endOfWeekFormatting = dateFormat.format(endOfWeekCalendar) // 2023-06-18로 변환

        _startOfWeek.value = startOfWeekFormatting
        _endOfWeek.value = endOfWeekFormatting

        viewModelScope.launch {

            val weekKcalArray = mutableListOf<Int>()
            val weekDateArray = mutableListOf<String>()

            getUserWeekKcalUseCase(userId).addListenerForSingleValueEvent(object :
                ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    calendar.time = startOfWeekCalendar

                    while (calendar.time <= endOfWeekCalendar) {

                        val currentDate = dateFormat.format(calendar.time) // 2023-06-12 ~ 2023-06-18 이런식으로 1주일 단위
                        calendar.add(Calendar.DAY_OF_WEEK, 1)

                        var dayKcal = 0

                        for (data in snapshot.children) {

                            val date = data.key?.substring(0, 10)

                            if (date == currentDate) {
                                val kcal = data.child("kcal").value.toString().toInt()
                                dayKcal += kcal
                            }

                        }
                        weekKcalArray.add(dayKcal)
                        weekDateArray.add(currentDate.substring(8, 10))

                    }
                    _weekKcalArray.value = Event(weekKcalArray)
                    _weekDateArray.value = Event(weekDateArray)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }
    }

    fun getPreviousWeekData(userId: String, previousStartOfWeek: String, previousEndOfWeek: String) {

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        _startOfPreviousWeek.value = previousStartOfWeek
        _endOfPreviousWeek.value = previousEndOfWeek

        viewModelScope.launch {

            val previousWeekKcalArray = mutableListOf<Int>()
            val previousWeekDateArray = mutableListOf<String>()

            _previousWeekKcalArray.value = Event(previousWeekKcalArray)
            _previousWeekDateArray.value = Event(previousWeekDateArray)

            getUserWeekKcalUseCase(userId).addListenerForSingleValueEvent(object :
                ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    calendar.time = dateFormat.parse(previousStartOfWeek) as Date

                    while (calendar.time <= dateFormat.parse(previousEndOfWeek)) {

                        val previousDate = dateFormat.format(calendar.time) // 2023-06-12 ~ 2023-06-18 이런식으로 1주일 단위
                        calendar.add(Calendar.DAY_OF_WEEK, 1)

                        var dayKcal = 0

                        for (data in snapshot.children) {

                            val date = data.key?.substring(0, 10)

                            if (date == previousDate) {
                                val kcal = data.child("kcal").value.toString().toInt()
                                dayKcal += kcal
                            }

                        }
                        previousWeekKcalArray.add(dayKcal)
                        previousWeekDateArray.add(previousDate.substring(8, 10))

                    }
                    _previousWeekKcalArray.value = Event(previousWeekKcalArray)
                    _previousWeekDateArray.value = Event(previousWeekDateArray)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }
    }

    fun getNextWeekData(userId: String, nextStartOfWeek: String, nextEndOfWeek: String) {

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        _startOfNextWeek.value = nextStartOfWeek
        _endOfNextWeek.value = nextEndOfWeek

        viewModelScope.launch {

            val nextWeekKcalArray = mutableListOf<Int>()
            val nextWeekDateArray = mutableListOf<String>()

            _nextWeekKcalArray.value = Event(nextWeekKcalArray)
            _nextWeekDateArray.value = Event(nextWeekDateArray)

            getUserWeekKcalUseCase(userId).addListenerForSingleValueEvent(object :
                ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    calendar.time = dateFormat.parse(nextStartOfWeek) as Date

                    while (calendar.time <= dateFormat.parse(nextEndOfWeek)) {

                        val nextDate = dateFormat.format(calendar.time) // 2023-06-12 ~ 2023-06-18 이런식으로 1주일 단위
                        calendar.add(Calendar.DAY_OF_WEEK, 1)

                        var dayKcal = 0

                        for (data in snapshot.children) {

                            val date = data.key?.substring(0, 10)

                            if (date == nextDate) {
                                val kcal = data.child("kcal").value.toString().toInt()
                                dayKcal += kcal
                            }

                        }
                        nextWeekKcalArray.add(dayKcal)
                        nextWeekDateArray.add(nextDate.substring(8, 10))
                    }
                    _nextWeekKcalArray.value = Event(nextWeekKcalArray)
                    _nextWeekDateArray.value = Event(nextWeekDateArray)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }
    }

    fun updateChartData(){

        var sumKcal = 0

        _entries.clear()

        try {
            viewModelScope.launch {
                for (i in 0..6) {
                    _entries.add(
                        Entry(
                            i.toFloat(),
                            _weekKcalArray.value?.peekContent()?.get(i)?.toFloat() ?: 0.0F
                        )
                    )
                    sumKcal += _weekKcalArray.value!!.peekContent()[i]

                }


            }
        } catch (e: Exception) {
            Log.e("currentError", e.message.toString())
        }

        _weekKcalSum.value = sumKcal
        Log.d("weight1", _weight.value.toString())
        try {
            _howMuchRunning.value =
                roundToFirstDecimalPlace(sumKcal / (((4.0 * (3.5 * _weight.value!! * 60)) / 1000) * 5).toFloat()) // 이런것도 메서드로 다 일치시켜야함 알아보기 힘들다.
        } catch (e: NullPointerException) {
            Log.e("runningNull", "아휴 또 난리야?")
        }



//

    }

    fun updateChartPreviousData(){

        var sumKcal = 0
        _entries.clear()

        try {
            for (i in 0..6) {
                _entries.add(
                    Entry(
                        i.toFloat(),
                        _previousWeekKcalArray.value?.peekContent()?.get(i)?.toFloat() ?: 0.0F
                    )
                )
                sumKcal += _previousWeekKcalArray.value!!.peekContent()[i]
            }
        } catch (e: Exception) {
            Log.e("previousError", e.message.toString())
        }

        _weekKcalSum.value = sumKcal
        Log.d("weight2", _weight.value.toString())
        try {
            _howMuchRunning.value =
                roundToFirstDecimalPlace(sumKcal / (((4.0 * (3.5 * _weight.value!! * 60)) / 1000) * 5).toFloat())
        } catch (e: NullPointerException) {
            Log.e("runningNull", "아휴 또 난리야?")
        }

    }

    fun updateChartNextData(){

        var sumKcal = 0
        _entries.clear()

        try {
            for (i in 0..6) {
                _entries.add(
                    Entry(
                        i.toFloat(),
                        _nextWeekKcalArray.value?.peekContent()?.get(i)?.toFloat() ?: 0.0F
                    )
                )
                sumKcal += _nextWeekKcalArray.value!!.peekContent()[i]
            }
        } catch (e: Exception) {
            Log.e("nextError", e.message.toString())
        }

        _weekKcalSum.value = sumKcal
        Log.d("weight3", _weight.value.toString())
        try {
            _howMuchRunning.value =
                roundToFirstDecimalPlace(sumKcal / (((4.0 * (3.5 * _weight.value!! * 60)) / 1000) * 5).toFloat())
            Log.d("runningValue", _howMuchRunning.value.toString())
        } catch (e: NullPointerException) {
            Log.e("runningNull", "아휴 또 난리야?")
        }

    }

    fun movePreviousWeek() {

        val currentStartOfWeek = _startOfWeek.value
        val currentEndOfWeek = _endOfWeek.value

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()

        calendar.time = dateFormat.parse(currentEndOfWeek)
        calendar.add(Calendar.DAY_OF_WEEK, -7)
        val newEndOfWeek = dateFormat.format(calendar.time)

        calendar.time = dateFormat.parse(newEndOfWeek)
        calendar.add(Calendar.DAY_OF_WEEK, -6)
        val newStartOfWeek = dateFormat.format(calendar.time)

        _startOfWeek.value = newStartOfWeek
        _endOfWeek.value = newEndOfWeek

        _isNextButtonEnabled.value = true
    }

    fun moveNextWeek() {

        val currentStartOfWeek = _startOfWeek.value
        val currentEndOfWeek = _endOfWeek.value

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        val currentDate = calendar.time

        val endOfWeekDate = dateFormat.parse(currentEndOfWeek)
        val isThisWeekPast = endOfWeekDate.before(currentDate)

        calendar.time = dateFormat.parse(currentStartOfWeek)
        calendar.add(Calendar.DAY_OF_WEEK, 7)
        val newStartOfWeek = dateFormat.format(calendar.time)

        calendar.time = dateFormat.parse(currentEndOfWeek)
        calendar.add(Calendar.DAY_OF_WEEK, 7)
        val newEndOfWeek = dateFormat.format(calendar.time)

        _startOfWeek.value = newStartOfWeek
        _endOfWeek.value = newEndOfWeek


    }

    fun roundToFirstDecimalPlace(number: Float): Float {
        return ((number * 10.0).toInt() / 10.0).toFloat()
    }


}