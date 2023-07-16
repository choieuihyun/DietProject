package com.myproject.dietproject.presentation.ui.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.myproject.dietproject.domain.model.TodayKcalForCalendar
import com.myproject.dietproject.domain.usecase.GetUserRecommendKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserTodayKcalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class CalendarDetailViewModel @Inject constructor(
    private val getUserTodayKcalUseCase: GetUserTodayKcalUseCase,
    private val getUserRecommendKcalUseCase: GetUserRecommendKcalUseCase
) : ViewModel() {

    private val _dayKcal: MutableLiveData<Int> = MutableLiveData()
    val dayKcal: LiveData<Int> = _dayKcal

    private val _dayKcalList: MutableLiveData<ArrayList<TodayKcalForCalendar?>> = MutableLiveData()
    val dayKcalList: LiveData<ArrayList<TodayKcalForCalendar?>> = _dayKcalList

    private var _recommendKcal: MutableLiveData<Int> = MutableLiveData()
    val recommendKcal: LiveData<Int> = _recommendKcal

    private var _imageResultLiveData: MutableLiveData<Int> = MutableLiveData()
    val imageResultLiveData: LiveData<Int> = _imageResultLiveData

    private var _calendarDetailAlert: MutableLiveData<String> = MutableLiveData()
    val calendarDetailAlert: LiveData<String> = _calendarDetailAlert

    fun getCalendarDetailData(userId: String, date: String) {

        viewModelScope.launch(Dispatchers.IO) {

            var sum = 0
            val kcalArrayList = ArrayList<TodayKcalForCalendar?>()

            getUserTodayKcalUseCase(userId, date).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (data in snapshot.children) {
                        val dateData = data.key?.substring(0, 10)

                        if (dateData == date) {
                            val kcal = data.child("kcal").value
                            sum += kcal.toString().toInt()
                            val todayKcalForCalendar =
                                TodayKcalForCalendar( // 이 구조가 아닌거 같음. 그냥 받아오는 데이터로 해야지 이건 아니다.
                                    kcal = kcal.toString().toFloat().roundToInt(),
                                    foodName = data.child("foodName").value.toString(),
                                    makerName = data.child("makerName").value.toString()
                                )
                            kcalArrayList.add(todayKcalForCalendar)
                        }

                    }

                    _dayKcalList.value = kcalArrayList // 어차피 읽어오기만 하니까 읽기 전용 리스트로의 변환인 listOf
                    _dayKcal.value = sum

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

    }

    fun getRecommendKcalData(userId: String) {

        viewModelScope.launch(Dispatchers.IO) {

            getUserRecommendKcalUseCase(userId).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    _recommendKcal.value = snapshot.value.toString().toInt()
                    imageSetting()

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

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