package com.myproject.dietproject.presentation.ui.weightChart

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.utils.EntryXComparator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.myproject.dietproject.domain.model.TodayKcal
import com.myproject.dietproject.domain.usecase.GetUserTodayKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserWeekKcalUseCase
import com.myproject.dietproject.presentation.ui.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
    private val getUserWeekKcalUseCase: GetUserWeekKcalUseCase
) : ViewModel() {


    private var _startOfWeek: MutableLiveData<String> = MutableLiveData()
    val startOfWeek: LiveData<String>
        get() = _startOfWeek

    private var _endOfWeek: MutableLiveData<String> = MutableLiveData()
    val endOfWeek: LiveData<String>
        get() = _endOfWeek

    private var _startOfPreviousWeek: MutableLiveData<String> = MutableLiveData()
    val startOfPreviousWeek: LiveData<String> = _startOfWeek

    private var _endOfPreviousWeek: MutableLiveData<String> = MutableLiveData()
    val endOfPreviousWeek: LiveData<String> = _endOfWeek

    private var _weekKcalArray: MutableLiveData<Event<MutableList<Int>>> = MutableLiveData()
    val weekKcalArray: LiveData<Event<MutableList<Int>>>
        get() = _weekKcalArray

    private var _weekDateArray: MutableLiveData<Event<MutableList<String>>> = MutableLiveData()
    val weekDateArray: LiveData<Event<MutableList<String>>>
        get() = _weekDateArray

    private var _previousWeekDateArray: MutableLiveData<Event<MutableList<String>>> = MutableLiveData()
    val previousWeekDateArray: LiveData<Event<MutableList<String>>>
        get() = _previousWeekDateArray

    private var _previousWeekKcalArray: MutableLiveData<Event<MutableList<Int>>> = MutableLiveData()
    val previousWeekKcalArray: LiveData<Event<MutableList<Int>>>
        get() = _previousWeekKcalArray

    private val _isNextButtonEnabled: MutableLiveData<Boolean> = MutableLiveData(true)
    val isNextButtonEnabled: LiveData<Boolean>
        get() = _isNextButtonEnabled

    private val _entries: MutableList<Entry> = mutableListOf()
    val entries: MutableList<Entry>
        get() = _entries


    fun getChartWeekData(userId: String) {

        val calendar = Calendar.getInstance()
        val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR) // 24(24번째 주) 이렇게 몇번째 주로 숫자로 나온다.

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val startOfWeekCalendar =
            calendar.time // Mon Jun 12 00:59:28 GMT+09:00 2023 시작을 월요일 ( 해당 주를 받아서 )

        calendar.add(Calendar.DAY_OF_WEEK, 6)
        val endOfWeekCalendar = calendar.time // Sun Jun 18 00:59:28 GMT+09:00 2023 해당 주의 마지막, 일요일

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val startOfWeekFormatting = dateFormat.format(startOfWeekCalendar) // 2023-06-12로 변환
        val endOfWeekFormatting = dateFormat.format(endOfWeekCalendar) // 2023-06-18로 변환

        _startOfWeek.value = startOfWeekFormatting
        _endOfWeek.value = endOfWeekFormatting

        Log.d("endof",dateFormat.parse(endOfWeekFormatting).toString())

        viewModelScope.launch {

            val weekKcalArray = mutableListOf<Int>()
            val weekDateArray = mutableListOf<String>()

            getUserWeekKcalUseCase(userId).addListenerForSingleValueEvent(object :
                ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    calendar.time = startOfWeekCalendar

                    while (calendar.time <= endOfWeekCalendar) {

                        val currentDate = dateFormat.format(calendar.time) // 2023-06-12 ~ 2023-06-18 이런식으로 1주일 단위
                        Log.d("newCurrentDate", currentDate)
                        calendar.add(Calendar.DAY_OF_WEEK, 1)

                        var sum = 0

                        for (data in snapshot.children) {

                            val date = data.key?.substring(0, 10)

                            if (date == currentDate) {
                                val kcal = data.child("kcal").value.toString().toInt()
                                sum += kcal
                                Log.d("currentKcal", kcal.toString())
                            }

                        }
                        weekKcalArray.add(sum)
                        weekDateArray.add(currentDate.substring(8, 10))
                    }
                    _weekKcalArray.value = Event(weekKcalArray)
                    _weekDateArray.value = Event(weekDateArray)

                    Log.d("currentKcalList", _weekKcalArray.value.toString())
                    Log.d("currentDateList", _weekDateArray.value.toString())
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
                        Log.d("newPreviousDate", previousDate)
                        calendar.add(Calendar.DAY_OF_WEEK, 1)

                        var sum = 0

                        for (data in snapshot.children) {

                            val date = data.key?.substring(0, 10)

                            if (date == previousDate) {
                                val kcal = data.child("kcal").value.toString().toInt()
                                sum += kcal
                                Log.d("previousKcal", kcal.toString())
                            }

                        }
                        previousWeekKcalArray.add(sum)
                        previousWeekDateArray.add(previousDate.substring(8, 10))
                    }
                    _previousWeekKcalArray.value = Event(previousWeekKcalArray)
                    _previousWeekDateArray.value = Event(previousWeekDateArray)

//                    Log.d("previousKcalList", _previousWeekKcalArray.value.toString())
//                    Log.d("previousDateList", _previousWeekDateArray.value.toString())
//                    Log.d("previousEntry", _entries.toString())

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    fun updateChartData(){

        _entries.clear()

        try {
            viewModelScope.launch {
                for (i in 0..6) {
                    _entries.add(
                        Entry(
                            _weekDateArray.value?.peekContent()?.get(i)?.toFloat() ?: 0.0F,
                            _weekKcalArray.value?.peekContent()?.get(i)?.toFloat() ?: 0.0F
                        )
                    )
                    Log.d("currentEntry1", _weekDateArray.value?.peekContent()?.get(i).toString())
                    Log.d("currentEntry2", _entries.toString())
                }
//                Collections.sort(
//                    _entries,
//                    EntryXComparator()
//                ) // 이거 안쓰면 다른화면 갔다가 차트올때 음수 배열 에러 뜸. 공식 깃허브 피셜 해결방법. 근데 이제 안해도 되네? 왜지?
            }
        } catch (e: Exception) {
            Log.e("currentError", e.message.toString())
        }
    }

    fun updateChartPreviousData(){

        _entries.clear()

        try {
            for (i in 0..6) {
                _entries.add(
                    Entry(
                        _previousWeekDateArray.value?.peekContent()?.get(i)?.toFloat() ?: 0.0F,
                        _previousWeekKcalArray.value?.peekContent()?.get(i)?.toFloat() ?: 0.0F
                    )
                )
            }


        } catch (e: Exception) {
            Log.e("previousError", e.message.toString())
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

    fun resetPreviousWeekData() {

//        _previousWeekKcalArray.value?.clear()
//        _previousWeekDateArray.value?.clear()

    }


}