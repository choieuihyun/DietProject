package com.myproject.dietproject.presentation.ui.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.myproject.dietproject.domain.model.TodayKcal
import com.myproject.dietproject.domain.model.TodayKcalForCalendar
import com.myproject.dietproject.domain.usecase.GetUserTodayKcalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarDetailViewModel @Inject constructor(
    private val getUserTodayKcalUseCase: GetUserTodayKcalUseCase
) : ViewModel() {

    private val _dayKcal: MutableLiveData<Int> = MutableLiveData()
    val dayKcal: LiveData<Int> = _dayKcal

    private val _dayKcalList: MutableLiveData<ArrayList<TodayKcalForCalendar?>> = MutableLiveData()
    val dayKcalList: LiveData<ArrayList<TodayKcalForCalendar?>> = _dayKcalList

    fun getCalendarDetailData(userId: String, date: String) {

        viewModelScope.launch(Dispatchers.IO) {

            var sum = 0
            val kcalArrayList = ArrayList<TodayKcalForCalendar?>()

            getUserTodayKcalUseCase(userId,date).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (data in snapshot.children) {
                        val dataDate = data.key?.substring(0, 10)

                        if (dataDate == date) {
                            val kcal = data.child("kcal").value
                            sum += kcal.toString().toInt()
                            val todayKcalForCalendar = TodayKcalForCalendar(
                                kcal = kcal.toString().toFloat(),
                                foodName = data.child("foodName").value.toString(),
                                makerName = data.child("makerName").value.toString()
                            )
                            Log.d("calendarDetail4", todayKcalForCalendar.toString())
                            kcalArrayList.add(todayKcalForCalendar)
                        }

                    }

                    _dayKcalList.value = kcalArrayList // 어차피 읽어오기만 하니까 읽기 전용 리스트로의 변환인 listOf
                    _dayKcal.value = sum

                    Log.d("calendarDetail1", _dayKcalList.value.toString())
                    Log.d("calendarDetail2", _dayKcal.value.toString())
                    Log.d("calendarDetail3", date.toString())

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

    }

}